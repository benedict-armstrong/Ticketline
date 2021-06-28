package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangeBookingDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import at.ac.tuwien.sepm.groupphase.backend.service.PdfService;
import at.ac.tuwien.sepm.groupphase.backend.service.SimpleMailService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import com.github.javafaker.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@EnableScheduling
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BookingRepository bookingRepository;
    private final TicketService ticketService;
    private final UserService userService;
    private final FileService fileService;
    private final SimpleMailService simpleMailService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              @Lazy TicketService ticketService,
                              UserService userService,
                              FileService fileService,
                              SimpleMailService simpleMailService, AuthenticationFacade authenticationFacade) {
        this.bookingRepository = bookingRepository;
        this.ticketService = ticketService;
        this.userService = userService;
        this.fileService = fileService;
        this.simpleMailService = simpleMailService;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public Booking save(Set<Ticket> tickets, Booking.Status status) {
        LOGGER.info("saveBooking({})", tickets);
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        File invoice;

        if (status == Booking.Status.RESERVED) {
            invoice = null;
        } else {
            PdfService pdf = new PdfService(user);
            pdf.createInvoice(tickets, fileService.getPdfCount());
            invoice = fileService.addFile(pdf.getFile());
        }

        Booking booking = Booking.builder()
            .user(user)
            .createDate(LocalDateTime.now())
            .tickets(tickets)
            .invoice(invoice)
            .status(status)
            .build();
        LOGGER.info(booking.toString());

        Booking savedBooking = bookingRepository.save(booking);

        List<File> pdfs = new ArrayList<>();
        pdfs.add(savedBooking.getInvoice());
        pdfs.add(
            ticketService.getPdf(savedBooking.getTickets().iterator().next().getPerformance().getId())
        );

        sendPurchaseConfirmationEmail(user, pdfs);
        return savedBooking;
    }

    @Override
    public Booking save(Booking booking) {
        LOGGER.trace("saveBooking({})", booking);
        ApplicationUser user = userService.findApplicationUserByEmail(authenticationFacade.getAuthentication().getPrincipal().toString(), false);
        booking.setUser(user);
        booking.setCreateDate(LocalDateTime.now());
        booking.setInvoice(null);
        if (booking.getStatus() == null) {
            booking.setStatus(Booking.Status.RESERVED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(ChangeBookingDto booking) {
        LOGGER.trace("update({})", booking);
        Booking.Status newStatus = Booking.Status.valueOf(booking.getStatus());
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        Booking oldBooking = bookingRepository.findByUserAndId(user, booking.getId());
        Booking.Status oldStatus = oldBooking.getStatus();
        List<File> pdfs = new ArrayList<>();

        //Booking.Status changed
        if (booking.getStatus() != oldStatus.toString()) {
            Ticket.Status status;

            switch (newStatus) {
                case PAID_FOR:
                    status = Ticket.Status.PAID_FOR;
                    PdfService pdf = new PdfService(user);
                    pdf.createInvoice(oldBooking.getTickets(), fileService.getPdfCount());
                    File invoice = fileService.addFile(pdf.getFile());
                    oldBooking.setInvoice(invoice);

                    pdfs.add(invoice);
                    pdfs.add(
                        ticketService.getPdf(oldBooking.getTickets().iterator().next().getPerformance().getId())
                    );

                    sendPurchaseConfirmationEmail(user, pdfs);
                    break;
                case RESERVED:
                    status = Ticket.Status.RESERVED;
                    break;
                case CANCELLED:
                    status = Ticket.Status.CANCELLED;
                    if (oldStatus != Booking.Status.RESERVED) {
                        PdfService stornoPdf = new PdfService(user);
                        stornoPdf.createStorno(oldBooking.getTickets(), fileService.getPdfCount());
                        File storno = fileService.addFile(stornoPdf.getFile());
                        oldBooking.setInvoice(storno);

                        pdfs.add(storno);
                        sendStornoConfirmationEmail(user, pdfs);
                    }
                    break;
                default: status = Ticket.Status.RESERVED;
            }

            ticketService.updateStatus(oldBooking.getTickets(), status);
        }

        if (oldStatus == Booking.Status.RESERVED && newStatus == Booking.Status.CANCELLED) {
            bookingRepository.delete(oldBooking);
            return null;
        } else {
            oldBooking.setStatus(newStatus);
            return bookingRepository.save(oldBooking);
        }
    }

    @Override
    public List<Booking> getBookings() {
        LOGGER.trace("getBookings()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        return bookingRepository.findByUser(user);
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        LOGGER.trace("deleteTicket({})", ticket);
        Booking booking = bookingRepository.findByTicketsContaining(ticket);
        Set<Ticket> tickets = booking.getTickets();
        tickets.remove(ticket);
        if (tickets.size() != 0) {
            booking.setTickets(tickets);
            bookingRepository.save(booking);
        } else {
            bookingRepository.delete(booking);
        }
    }

    private void sendPurchaseConfirmationEmail(ApplicationUser user, List<File> attachments) {
        simpleMailService.sendMail(user.getEmail(), "[Ticketline] Invoice for your recently bought tickets",
            String.format("<h1> Hello %s %s, </h1></br></br>Attached you can download the invoice and the tickets for your recent purchase.",
                user.getFirstName(), user.getLastName()),
            attachments
        );
    }

    private void sendStornoConfirmationEmail(ApplicationUser user, List<File> attachments) {
        simpleMailService.sendMail(user.getEmail(), "[Ticketline] Storno invoice for your cancelled purchase",
            String.format("<h1> Hello %s %s, </h1></br></br>Attached you can download the storno invoice for the purchase you recently cancelled.",
                user.getFirstName(), user.getLastName()),
            attachments
        );
    }
}
