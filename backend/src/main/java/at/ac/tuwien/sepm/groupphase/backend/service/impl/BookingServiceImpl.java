package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangeBookingDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@EnableScheduling
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public BookingServiceImpl(BookingRepository  bookingRepository,
                              TicketRepository ticketRepository,
                              UserService userService,
                              AuthenticationFacade authenticationFacade) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.ticketRepository = ticketRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public Booking save(Set<Ticket> tickets, Booking.Status status) {
        LOGGER.trace("saveBooking({})", tickets);
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        Booking booking = Booking.builder()
            .user(user)
            .createDate(LocalDateTime.now())
            .tickets(tickets)
            .invoice(null)
            .status(status)
            .build();

        return bookingRepository.save(booking);
    }

    @Override
    public Booking save(Booking booking) {
        LOGGER.trace("saveBooking({})", booking);
        booking.setUser(
            userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal())
        );
        booking.setCreateDate(LocalDateTime.now());
        booking.setInvoice(null);
        if (booking.getStatus() == null) {
            booking.setStatus(Booking.Status.RESERVED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(ChangeBookingDto booking) {
        Booking.Status newStatus = Booking.Status.valueOf(booking.getStatus());
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        Booking oldBooking = bookingRepository.findByUserAndId(user, booking.getId());
        LOGGER.info(oldBooking.toString());


        //Booking.Status changed
        if (booking.getStatus() != oldBooking.getStatus().toString()) {
            Ticket.Status status;

            switch (newStatus) {
                case PAID_FOR:
                    status = Ticket.Status.PAID_FOR;
                    break;
                case RESERVED:
                    status = Ticket.Status.RESERVED;
                    break;
                case CANCELLED:
                    status = Ticket.Status.CANCELLED;
                    break;
                default: status = Ticket.Status.RESERVED;
            }

            if (status != Ticket.Status.CANCELLED) {
                System.out.println("not canceled");
                Set<Ticket> tickets = oldBooking.getTickets();

                for (Ticket ticket : tickets) {
                    ticket.setStatus(status);
                }

                ticketRepository.saveAll(tickets);
            }
        }

        oldBooking.setStatus(newStatus);
        return bookingRepository.save(oldBooking);
    }

    @Override
    public List<Booking> getBookings() {
        LOGGER.trace("getBookings()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return bookingRepository.findByUser(user);
    }
}
