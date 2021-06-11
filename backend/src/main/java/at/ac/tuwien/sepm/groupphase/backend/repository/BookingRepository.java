package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
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
}
