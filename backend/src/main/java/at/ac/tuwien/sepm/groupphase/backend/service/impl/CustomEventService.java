package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    @Autowired
    public CustomEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAll() {
        LOGGER.debug("Get all events");
        return eventRepository.findAll();
    }

    @Override
    public Event findById(long id) {
        LOGGER.debug("Get event by id {}", id);
        Optional<Event> temp = eventRepository.findById(id);
        if (temp.isPresent()) {
            return temp.get();
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Event addEvent(Event event) {
        LOGGER.trace("addEvent({})", event);
        return eventRepository.save(event);
    }
}
