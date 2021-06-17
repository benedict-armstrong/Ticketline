package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;

import java.util.List;
import java.util.Set;

public interface BookingService {

    /**
     * Saves a booking in the database.
     *
     * @param tickets of the booking to be saved.
     * @param status of the booking to be saved.
     * @return the newly added booking.
     */
    Booking save(Set<Ticket> tickets, Booking.Status status);

    /**
     * Saves a booking in the database.
     *
     * @param booking to be saved.
     * @return the newly added booking.
     */
    Booking save(Booking booking);

    /**
     * Updates a booking in the database.
     *
     * @param booking to be updated.
     * @return the updated booking.
     */
    Booking update(Booking booking);

    /**
     * Gets all tickets in the cart of a User.
     *
     * @return list of tickets.
     */
    List<Booking> getBookings();
}
