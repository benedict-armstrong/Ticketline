package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NoTicketLeftException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.CartItemService;
import at.ac.tuwien.sepm.groupphase.backend.service.LayoutUnitService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@EnableScheduling
public class CartItemServiceImpl implements CartItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final BookingService bookingService;
    private final LayoutUnitService layoutUnitService;
    private final VenueService venueService;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               UserService userService,
                               AuthenticationFacade authenticationFacade,
                               BookingService bookingService,
                               LayoutUnitService layoutUnitService,
                               VenueService venueService) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.bookingService = bookingService;
        this.layoutUnitService = layoutUnitService;
        this.venueService = venueService;
    }

    @Override
    @Transactional
    public CartItem save(Performance performance, TicketType ticketType, CartItem.Status status, int amount) {
        LOGGER.trace("save({}, {},  {}, {})", performance, ticketType, status, amount);
        CartItem cartItem = CartItem.builder()
            .user(
                userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal())
            )
            .status(status)
            .changeDate(LocalDateTime.now())
            .build();

        Sector sector = ticketType.getSector();

        Venue venue = venueService.getOneById(performance.getVenue().getId());
        Set<Ticket> ticketSet = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            LayoutUnit seat = null;
            for (LayoutUnit spot : venue.getLayout()) {
                if (spot.getSector().getId().equals(sector.getId())) {
                    if (spot.getTaken() == null || !spot.getTaken()) {
                        seat = spot;
                        break;
                    }
                }
            }

            if (seat == null) {
                throw new NoTicketLeftException("No free seat was found.");
            }

            seat.setTaken(true);

            layoutUnitService.add(seat);

            Ticket ticket = Ticket.builder()
                .performance(performance)
                .ticketType(ticketType)
                .seat(seat)
                .build();
            ticketSet.add(ticket);
        }

        cartItem.setTickets(ticketSet);

        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem addTicket(Long id) {
        LOGGER.trace("addTicket({})", id);
        Optional<CartItem> loadedCartItem = cartItemRepository.findById(id);
        if (loadedCartItem.isPresent()) {
            CartItem cartItem = loadedCartItem.get();
            cartItem.setChangeDate(LocalDateTime.now());

            Ticket template = cartItem.getTickets().iterator().next();
            Sector sector = template.getTicketType().getSector();

            Venue venue = template.getPerformance().getVenue();
            LayoutUnit seat = null;
            for (LayoutUnit spot : venue.getLayout()) {
                if (spot.getSector().getId().equals(sector.getId())) {
                    if (spot.getTaken() == null || !spot.getTaken()) {
                        seat = spot;
                        break;
                    }
                }
            }
            if (seat == null) {
                throw new NoTicketLeftException("No free seat was found.");
            }

            seat.setTaken(true);

            layoutUnitService.add(seat);

            Ticket ticket = Ticket.builder()
                .ticketType(template.getTicketType())
                .performance(template.getPerformance())
                .seat(seat)
                .build();

            Set<Ticket> ticketSet = cartItem.getTickets();
            ticketSet.add(ticket);
            cartItem.setTickets(ticketSet);

            return cartItemRepository.save(cartItem);
        }
        throw new NotFoundException("This cartItem could not be found.");
    }

    @Override
    public List<CartItem> getCartItems(CartItem.Status status) {
        LOGGER.trace("getCartItems({})", status);
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return cartItemRepository.findByUserAndStatus(user, status);
    }

    @Override
    @Transactional
    public boolean checkout() {
        LOGGER.trace("checkout()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        List<CartItem> cartItems = cartItemRepository.findByUserAndStatus(user, CartItem.Status.IN_CART);
        if (cartItems.size() > 0) {
            for (CartItem cartItem : cartItems) {
                cartItem.setStatus(CartItem.Status.PAID_FOR);
            }
            cartItemRepository.saveAll(cartItems);

            Booking booking = Booking.builder().cartItems(new HashSet<>(cartItems)).build();
            bookingService.save(booking);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        LOGGER.trace("delete({})", id);

        Optional<CartItem> opCartItem = cartItemRepository.findById(id);

        if (opCartItem.isPresent()) {
            CartItem cartItem = opCartItem.get();
            Set<LayoutUnit> seats = new HashSet<>();
            for (Ticket ticket : cartItem.getTickets()) {
                LayoutUnit seat = ticket.getSeat();
                seat.setTaken(false);
                seats.add(seat);
            }
            layoutUnitService.saveAll(seats);
            cartItemRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteTicket(Long id, Long ticketId) {
        LOGGER.trace("deleteTicket({})", id);
        Optional<CartItem> opCartItem = cartItemRepository.findById(id);
        if (opCartItem.isPresent()) {
            CartItem cartItem = opCartItem.get();

            boolean returnVal = false;
            Set<Ticket> ticketSet = new HashSet<>();
            for (Ticket ticket : cartItem.getTickets()) {
                if (ticket.getId().equals(ticketId)) {
                    returnVal = true;
                    LayoutUnit seat = ticket.getSeat();
                    seat.setTaken(false);
                    layoutUnitService.add(seat);
                } else {
                    ticketSet.add(ticket);
                }
            }

            if (returnVal) {
                if (ticketSet.size() > 0) {
                    cartItem.setTickets(ticketSet);
                    cartItemRepository.save(cartItem);
                } else {
                    cartItemRepository.delete(cartItem);
                }
            }
            return returnVal;
        } else {
            throw new NotFoundException("The cartItem of the ticket you tried to delete was not found.");
        }
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void pruneCartItems() {
        LOGGER.trace("pruneCartItems()");
        List<CartItem> toBeDeleted = cartItemRepository.findByChangeDateBeforeAndStatus(LocalDateTime.now().minusMinutes(1), CartItem.Status.IN_CART);
        if (toBeDeleted.size() > 0) {
            LOGGER.info("Deleting {} stale cartItems from carts", toBeDeleted.size());
            Set<LayoutUnit> seats = new HashSet<>();
            for (CartItem cartItem : toBeDeleted) {
                for (Ticket ticket : cartItem.getTickets()) {
                    LayoutUnit seat = ticket.getSeat();
                    seat.setTaken(false);
                    seats.add(seat);
                }
            }
            layoutUnitService.saveAll(seats);
        }
        cartItemRepository.deleteAll(toBeDeleted);
    }
}
