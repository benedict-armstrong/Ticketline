package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;

import java.util.List;

public interface BookingService {

    /**
     * Saves a booking in the database.
     *
     * @param booking the booking to be saved.
     * @return the newly added booking.
     */
    Booking save(Booking booking);

    /**
     * Gets all tickets in the cart of a User.
     *
     * @return list of tickets.
     */
    List<Booking> getBookings();
}
