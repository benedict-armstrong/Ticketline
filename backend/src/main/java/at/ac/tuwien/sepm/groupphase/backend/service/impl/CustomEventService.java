package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public CustomEventService(EventRepository eventRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Event> findAllByDate(Pageable pageable) {
        LOGGER.debug("Get all events");
        return eventRepository.findAllByOrderByDateAsc(pageable).getContent();
    }

    @Override
    public Event findById(long id) {
        LOGGER.debug("Get event by id {}", id);
        return eventRepository.findOneById(id);
    }

    @Override
    public Event addEvent(Event event) {
        LOGGER.trace("addEvent({})", event);

        /*
        Artist artist = artistRepository.getOne(event.getArtist().getId());
        Set<Event> artistEvents;
        if (artist.getEvents() == null) {
            artistEvents = new HashSet<>();
        } else {
            artistEvents = artist.getEvents();
        }

        artistEvents.add(event);
        artist.setEvents(artistEvents);
         */

        return eventRepository.save(event);
    }
}