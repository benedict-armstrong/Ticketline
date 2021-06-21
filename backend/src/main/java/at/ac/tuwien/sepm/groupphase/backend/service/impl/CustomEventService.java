package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopEvent;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    @Autowired
    public CustomEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAllOrderedByStartDate(Pageable pageable) {
        LOGGER.trace("Get all events");
        return eventRepository.findAllByOrderByStartDateAsc(pageable).getContent();
    }

    @Override
    public Event findById(long id) {
        LOGGER.trace("Get event by id {}", id);
        return eventRepository.findOneById(id);
    }

    @Override
    public Event addEvent(Event event) {
        LOGGER.trace("addEvent({})", event);

        Set<Performance> performanceSet = event.getPerformances();

        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("Starting date of event must be before the ending date");
        }


        for (Performance performance : performanceSet) {
            if (performance.getDate().isBefore(event.getStartDate().atStartOfDay())
                || performance.getDate().isAfter(event.getEndDate().atStartOfDay())) {
                throw new IllegalArgumentException("Date of performances must be in period of event");
            }
        }

        Event eventCreated = eventRepository.save(event);

        for (Performance performance : performanceSet) {
            performance.setEvent(eventCreated);
        }

        eventCreated.setPerformances(performanceSet);

        return eventRepository.save(event);
    }

    @Override
    public List<Event> search(Event event, Pageable pageable) {
        LOGGER.trace("searchEvent({}, {}, {}, {})", event.getName(), event.getDescription(), event.getDuration(), event.getEventType());

        EventSpecificationBuilder builder = new EventSpecificationBuilder();

        if (event.getName() != null) {
            builder.with("name", ":", event.getName());
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

    @Override
    public List<TopEvent> findTopEvents(Pageable pageable) {
        LOGGER.trace("findTopEvents({})", pageable);

        List<Event> events = eventRepository.findAllByOrderByStartDateAsc(pageable).getContent();

        List<TopEvent> topEvents = new LinkedList<>();

        List<Long> soldTickets = eventRepository.findSoldTicketsOrderByStartDateAsc(pageable.getOffset(), pageable.getPageSize());

        for (int i = 0; i < events.size(); i++) {
            Set<Performance> performances = events.get(i).getPerformances();

            Long totalTickets = 0L;

            for (Performance performance : performances) {
                List<LayoutUnit> layout = performance.getVenue().getLayout();
                for (LayoutUnit layoutUnit : layout) {
                    if (!layoutUnit.getSector().getType().equals(Sector.SectorType.STAGE)) {
                        totalTickets++;
                    }
                }
            }

            topEvents.add(new TopEvent(events.get(i), totalTickets, soldTickets.get(i)));
        }

        return topEvents;
    }
}