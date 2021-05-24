package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    /**
     * Get all venues which were created by User.
     *
     * @param user User for which to find Venues
     * @return List of Venues
     */
    List<Venue> findAllByOwnerIs(ApplicationUser user);

}
