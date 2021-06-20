package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find all booking entries by user.
     *
     * @param user of the bookings to find
     * @return ordered list of all found bookings
     */
    List<Booking> findByUser(ApplicationUser user);

    /**
     * Find booking by user and id.
     *
     * @param user of the booking to find
     * @param id of the booking to find
     * @return ordered list of all found bookings
     */
    Booking findByUserAndId(ApplicationUser user, long id);

    Booking findByTicketsContaining(Ticket ticket);
}
