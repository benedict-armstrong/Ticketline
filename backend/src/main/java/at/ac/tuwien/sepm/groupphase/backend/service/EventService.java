package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    /**
     * add a new event.
     *
     * @param event to be added
     * @return the added event with id set
     */
    Event addEvent(Event event);


    /**
     * find all events by criteria, paginated.
     *
     * @param pageable pagination
     * @return list of events that meet criteria
     */
    List<Event> findAllByCriteria(Pageable pageable);


    /**
     * find a specific event by id.
     *
     * @param id of the event
     * @return event with the id
     */
    Event findById(Long id);
}
