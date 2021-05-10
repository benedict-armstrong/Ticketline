package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

import java.util.List;

public interface EventService {

    /**
     * Find all events.
     *
     * @return list of all events
     */
    List<Event> findAll();

    /**
     * Find event by id.
     *
     * @return event with id
     */
    Event findById(long id);

    /**
     * add a new event.
     *
     * @param event to be added
     * @return the added event with id set
     */
    Event addEvent(Event event);
}
