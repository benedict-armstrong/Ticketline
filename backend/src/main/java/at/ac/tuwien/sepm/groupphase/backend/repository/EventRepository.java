package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find one event by id.
     *
     * @param id of the event to find
     * @return event with the id
     */
    Event findOneById(Long id);
}
