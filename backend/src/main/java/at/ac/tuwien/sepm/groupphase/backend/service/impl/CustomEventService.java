package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomEventService implements EventService {
    private final EventRepository eventRepository;

    public CustomEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAllByCriteria(Pageable pageable) {
        return eventRepository.findAll(pageable).getContent();
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findOneById(id);
    }
}
