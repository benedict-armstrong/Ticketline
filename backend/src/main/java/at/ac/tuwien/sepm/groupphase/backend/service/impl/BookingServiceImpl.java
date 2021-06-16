package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public BookingServiceImpl(BookingRepository  bookingRepository,
                              UserService userService,
                              AuthenticationFacade authenticationFacade) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public Booking save(Booking booking) {
        LOGGER.trace("saveBooking({})", booking);
        booking.setUser(
            userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal())
        );
        booking.setBuyDate(LocalDateTime.now());
        booking.setInvoice(null);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookings() {
        LOGGER.trace("getBookings()");
        ApplicationUser user = userService.findApplicationUserByEmail((String) authenticationFacade.getAuthentication().getPrincipal());
        return bookingRepository.findByUser(user);
    }
}
