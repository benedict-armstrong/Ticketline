package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecification;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSearchCriteria;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    @Autowired
    public CustomEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAllOrderedByDate(Pageable pageable) {
        LOGGER.trace("Get all events");
        return eventRepository.findAllByOrderByDateAsc(pageable).getContent();
    }

    @Override
    public Event findById(long id) {
        LOGGER.trace("Get event by id {}", id);
        return eventRepository.findOneById(id);
    }

    @Override
    public Event addEvent(Event event) {
        LOGGER.trace("addEvent({})", event);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> search(Event event, Pageable pageable) {
        LOGGER.trace("searchEvent({}, {}, {}, {})", event.getTitle(), event.getDescription(), event.getDuration(), event.getEventType());

        EventSpecificationBuilder builder = new EventSpecificationBuilder();

        if (event.getTitle() != null) {
            builder.with("title", ":", event.getTitle());
        }
        if (event.getDescription() != null) {
            builder.with("description", ":", event.getDescription());
        }
        if (event.getDuration() != 0) {
            builder.with("duration", "+", event.getDuration());
        }
        if (event.getEventType() != null) {
            builder.with("eventType", ":", event.getEventType());
        }

        return eventRepository.findAll(builder.build(), pageable).getContent();
    }
}