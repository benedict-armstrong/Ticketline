package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatCount;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.FullCartException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NoTicketLeftException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.LayoutUnitService;
import at.ac.tuwien.sepm.groupphase.backend.service.PdfService;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.SimpleMailService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketTypeService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@EnableScheduling
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final PerformanceService performanceService;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;
    private final LayoutUnitService layoutUnitService;
    private final TicketTypeService ticketTypeService;
    private final SimpleMailService simpleMailService;

    private final Long maxCartSize = 10L;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserService userService,
                             AuthenticationFacade authenticationFacade,
                             BookingService bookingService,
                             PerformanceService performanceService,
                             LayoutUnitService layoutUnitService,
                             TicketTypeService ticketTypeService, SimpleMailService simpleMailService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
        this.performanceService = performanceService;
        this.layoutUnitService = layoutUnitService;
        this.ticketTypeService = ticketTypeService;

        this.simpleMailService = simpleMailService;
    }

    @Override
    public List<Ticket> createTicketsByAmount(Long performanceId, TicketType ticketType, Ticket.Status status, int amount) {
        LOGGER.trace("createTicketsByAmount({}, {},  {}, {})", performanceId, ticketType, status, amount);

        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        Performance performance = performanceService.findById(performanceId);


        List<Ticket> currentTickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (currentTickets.size() + amount > maxCartSize) {
            throw new FullCartException("Tickets were not added to cart, this request would exceed the cart size limit of " + maxCartSize + ".");
        }

        List<Ticket> ticketList = new LinkedList<>();
        Sector sector = ticketType.getSector();

        List<LayoutUnit> freeSeats = ticketRepository.getFreeSeatsInPerformanceAndSector(performance, sector);

        if (freeSeats.size() < amount) {
            if (amount > 1) {
                throw new NoTicketLeftException("Not enough free seats were found.");
            }
            throw new NoTicketLeftException("No free seat was found.");
        }

        for (int i = 0; i < amount; i++) {
            ticketList.add(
                Ticket.builder()
                    .ticketType(ticketType)
                    .performance(performance)
                    .status(status)
                    .user(user)
                    .changeDate(LocalDateTime.now())
                    .seat(freeSeats.get(i))
                    .build()
            );
        }

        return ticketRepository.saveAll(ticketList);
    }

    @Override
    public List<Ticket> createTicketBySeat(Long performanceId, TicketType ticketType, Ticket.Status status, Long seatId) {
        LOGGER.trace("createTicketBySeat({}, {},  {}, {})", performanceId, ticketType, status, seatId);

        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        Performance performance = performanceService.findById(performanceId);

        List<Ticket> currentTickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (currentTickets.size() + 1 > maxCartSize) {
            throw new FullCartException("Tickets were not added to cart, this request would exceed the cart size limit of " + maxCartSize + ".");
        }

        LayoutUnit seat = layoutUnitService.findById(seatId);

        if (seat == null) {
            throw new NotFoundException("This seat was not found");
        }

        List<Ticket> ticketList = new LinkedList<>();

        if (!ticketRepository.checkIfSeatIsFreeByPerformance(performance, seat)) {
            throw new NoTicketLeftException("This seat is not free anymore.");
        }

        Ticket ticket = Ticket.builder()
            .ticketType(ticketType)
            .performance(performance)
            .status(status)
            .user(user)
            .changeDate(LocalDateTime.now())
            .seat(seat)
            .build();

        ticketList.add(ticketRepository.save(ticket));

        return ticketList;
    }

    @Override
    public List<Ticket> getTickets(Ticket.Status status) {
        LOGGER.trace("getTickets({})", status);
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        List<Ticket> list = ticketRepository.findByUserAndStatus(user, status);
        List<Ticket> newList = new LinkedList<>();
        for (Ticket ticket : list) {
            Performance performance = ticket.getPerformance();
            Venue venue = performance.getVenue();
            List<Sector> sectorList = new LinkedList<>();
            for (Sector sector : venue.getSectors()) {
                boolean done = false;
                for (Sector newSector : sectorList) {
                    if (newSector.getId().equals(sector.getId())) {
                        done = true;
                        break;
                    }
                }
                if (!done) {
                    sectorList.add(sector);
                }
            }
            venue.setSectors(sectorList);
            performance.setVenue(venue);
            ticket.setPerformance(performance);
            newList.add(ticket);
        }
        return newList;
    }

    public List<LayoutUnit> getTakenSeatsInPerformance(Performance performance) {
        LOGGER.trace("getTakenSeatsInPerformance({})", performance);
        return ticketRepository.getTakenSeatsInPerformance(performance);
    }

    @Override
    public List<SeatCount> getSeatCountsInPerformance(Long performanceId) {
        LOGGER.trace("getSeatCountsInPerformanceBySector({})", performanceId);

        Performance performance = performanceService.findById(performanceId);

        List<SeatCount> seatCounts = new LinkedList<>();

        for (Sector sector : performance.getVenue().getSectors()) {
            List<LayoutUnit> freeSeats = ticketRepository.getFreeSeatsInPerformanceAndSector(performance, sector);

            List<LayoutUnit> totalSeats = layoutUnitService.findBySector(sector);

            seatCounts.add(SeatCount.builder()
                .sectorId(sector.getId())
                .freeSeats(freeSeats.size())
                .totalSeats(totalSeats.size())
                .build());
        }

        return seatCounts;
    }

    @Override
    @Transactional
    public boolean checkout() {
        LOGGER.trace("checkout()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        List<Ticket> tickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (tickets.size() < 1) {
            return false;
        }

        for (Ticket ticket : tickets) {
            ticket.setStatus(Ticket.Status.PAID_FOR);
        }
        ticketRepository.saveAll(tickets);
        bookingService.save(new HashSet<>(tickets), Booking.Status.PAID_FOR);
        return true;
    }

    @Override
    @Transactional
    public boolean reserve() {
        LOGGER.trace("reserve()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal(), false);
        List<Ticket> tickets = ticketRepository.findByUserAndStatus(user, Ticket.Status.IN_CART);
        if (tickets.size() < 1) {
            return false;
        }

        for (Ticket ticket : tickets) {
            ticket.setStatus(Ticket.Status.RESERVED);
        }
        ticketRepository.saveAll(tickets);
        bookingService.save(new HashSet<>(tickets), Booking.Status.RESERVED);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        LOGGER.trace("delete({})", id);

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);

        if (optionalTicket.isEmpty()) {
            return false;
        }
        ticketRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(List<Long> ids) {
        LOGGER.trace("delete({})", ids);
        List<Ticket> tickets = ticketRepository.findByIdList(ids);

        if (tickets.size() < 1) {
            return false;
        }

        ticketRepository.deleteAll(tickets);
        return true;
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void pruneTickets() {
        LOGGER.trace("pruneTickets()");
        List<Ticket> toBeDeleted = ticketRepository.findByChangeDateBeforeAndStatus(LocalDateTime.now().minusMinutes(1), Ticket.Status.IN_CART);
        if (toBeDeleted.size() > 0) {
            LOGGER.info("Deleting {} stale tickets from carts", toBeDeleted.size());
            ticketRepository.deleteAll(toBeDeleted);
        }
    }

    @Override
    @Transactional
    public void pruneReservations(List<Performance> performances) {
        LOGGER.trace("pruneReservations()");
        List<Ticket> tickets = new ArrayList<>();
        for (Performance performance : performances) {
            tickets.addAll(ticketRepository.findByPerformanceAndStatus(performance, Ticket.Status.RESERVED));
        }

        for (Ticket ticket : tickets) {
            bookingService.deleteTicket(ticket);
            ticketRepository.delete(ticket);
        }
    }

    @Override
    public void updateStatus(Set<Ticket> tickets, Ticket.Status status) {
        LOGGER.trace("updateStatus({}, {})", tickets, status);
        for (Ticket ticket : tickets) {
            ticket.setStatus(status);
        }

        ticketRepository.saveAll(tickets);
    }

    @Override
    public List<Ticket> getTicketsForPerformance(long performanceId, long userId) {
        LOGGER.trace("getTicketsForPerformance({} ,{})", performanceId, userId);
        Performance performance = performanceService.findById(performanceId);
        ApplicationUser user = userService.findApplicationUserByIdConfirmation(userId, true);

        return ticketRepository.findByPerformanceAndUserAndStatus(performance, user, Ticket.Status.PAID_FOR);
    }

    @Override
    public File getPdf(long performanceId) {
        LOGGER.trace("getPdf() {}", performanceId);
        ApplicationUser user = userService.findApplicationUserByEmail(authenticationFacade.getAuthentication().getPrincipal().toString(), false);
        List<Ticket> tickets = getTicketsForPerformance(performanceId, user.getId());
        PdfService pdf = new PdfService(user);
        pdf.createTicket(tickets);
        return pdf.getFile();
    }

    public Ticket updateTicket(Ticket ticket) {
        LOGGER.trace("updateTicket()");
        ApplicationUser user = userService.findApplicationUserByEmail(authenticationFacade.getMail(), false);
        Ticket oldTicket = ticketRepository.findTicketByUserAndStatusAndId(user, Ticket.Status.IN_CART, ticket.getId());

        if (oldTicket == null) {
            throw new NotFoundException("Ticket doesn't exist");
        } else {

            if (!ticket.getTicketType().getSector().equals(oldTicket.getTicketType().getSector())) {
                throw new IllegalArgumentException("Type must be for same Sector");
            }
            TicketType ticketType = ticketTypeService.getTicketTypeById(ticket.getTicketType().getId());
            oldTicket.setTicketType(ticketType);
            return ticketRepository.save(oldTicket);
        }
    }

    @Override
    public List<Double> getRelativeTicketSalesPastSevenDays() {
        LOGGER.trace("getTicketSalesPastSevenDays()");
        List<Long> list = new ArrayList<>();
        List<Double> out = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            list.add(ticketRepository.countTicketByChangeDateBetweenAndStatus(day.atStartOfDay(), day.plusDays(1).atStartOfDay(), Ticket.Status.PAID_FOR));
        }

        Long max = Collections.max(list);

        if (max == 0L) {
            for (Long ignored : list) {
                out.add(0.0d);
            }
        } else {
            for (Long count : list) {
                out.add((double) count / max);
            }
        }

        return out;
    }
}
