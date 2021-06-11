package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
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
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public BookingServiceImpl(BookingRepository  bookingRepository,
                              UserRepository userRepository,
                              AuthenticationFacade authenticationFacade) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public Booking save(Set<Ticket> tickets) {
        LOGGER.trace("saveBooking({})", tickets);
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        Booking booking = Booking.builder()
            .user(user)
            .createDate(LocalDateTime.now())
            .tickets(tickets)
            .invoice(null)
            .build();

        return bookingRepository.save(booking);
    }

    @Override
    public Booking save(Booking booking) {
        booking.setCreateDate(LocalDateTime.now());
        booking.setUser(userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal()));
        booking.setInvoice(null);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookings() {
        LOGGER.trace("getBookings()");
        ApplicationUser user = userRepository.findUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return bookingRepository.findByUser(user);
    }
}
