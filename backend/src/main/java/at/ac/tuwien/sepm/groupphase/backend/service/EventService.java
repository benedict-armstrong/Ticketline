package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    /**
     * Find all events with pagination sorted by date.
     *
     * @return list of all events
     */
    List<Event> findAllOrderedByStartDate(Pageable pageable);

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

    /**
     * Returns a list of events which match the search values.
     *
     * @param event with the values to search
     * @return list of events with all possible values
     */
    List<Event> search(Event event, Pageable pageable);

    /**
     * Returns a list of events which match the full text search.
     *
     * @param text to search with
     * @return list of events with all possible values
     */
    List<Event> search(String text, Pageable pageable);
}
