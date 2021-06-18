package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BookingMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/bookings")
public class BookingEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingEndpoint(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user cart")
    public List<BookingDto> getBookings() {
        LOGGER.info("GET /api/v1/bookings");
        return bookingMapper.bookingListToBookingDtoList(bookingService.getBookings());
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a booking for the user")
    public BookingDto createCartTicket(@RequestBody BookingDto booking) {
        LOGGER.info("POST /api/v1/bookings {}", booking);
        return bookingMapper.bookingToBookingDto(bookingService.save(bookingMapper.bookingDtoToBooking(booking)));
    }

    @PutMapping("/change")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates bookings and its tickets")
    public BookingDto reserve(@RequestBody BookingDto booking) {
        LOGGER.info("PUT /api/v1/bookings/change");
        /*
        Booking book = bookingMapper.bookingDtoToBooking(booking);
        LOGGER.info(book.toString());
        return bookingMapper.bookingToBookingDto(bookingService.update(book));*/
        return null;
    }
}
