package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public CustomEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
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
    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public List<Event> search(String text, Pageable pageable) {
        LOGGER.trace("searchEvent({})", text);

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        // Only on first start to index everything, after that ORM will synchronize
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e.getMessage());
        }

        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                            .buildQueryBuilder().forEntity(Event.class).get();

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        List<String> textList = Arrays.asList(text.split(" "));

        for (String txt : textList) {
            booleanQuery.add(qb.keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onField("name")
                .andField("performances.title")
                .andField("performances.artist.firstName")
                .andField("performances.artist.lastName")
                .matching(txt.toLowerCase())
                .createQuery(), BooleanClause.Occur.SHOULD);
            booleanQuery.add(qb.keyword()
                .onField("performances.artist.firstName")
                .andField("performances.artist.lastName")
                .matching(txt.toLowerCase())
                .createQuery(), BooleanClause.Occur.SHOULD);
        }

        Query query = qb.bool()
                        .should(
                            qb.keyword()
                            .fuzzy()
                            .withEditDistanceUpTo(2)
                            .withPrefixLength(0)
                            .onField("name")
                            .andField("description")
                            .andField("performances.title")
                            .andField("performances.description")
                            .andField("performances.artist.firstName")
                            .andField("performances.artist.lastName")
                            .matching(text.toLowerCase())
                            .createQuery())
                        .should(
                            qb.keyword()
                            .onField("description")
                            .andField("performances.description")
                            .matching(text.toLowerCase())
                            .createQuery())
                        .should(booleanQuery.build())
            .createQuery();

        // wrap Lucene query in a javax.persistence.Query
        FullTextQuery jpaQuery =
            fullTextEntityManager.createFullTextQuery(query, Event.class);

        jpaQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

        // execute search
        return (List<Event>) jpaQuery.getResultList();

    }
}