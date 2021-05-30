package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import at.ac.tuwien.sepm.groupphase.backend.specification.PerformanceSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomPerformanceService implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PerformanceRepository performanceRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CustomPerformanceService(PerformanceRepository performanceRepository,  EventRepository eventRepository) {
        this.performanceRepository = performanceRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Performance findById(long id) {
        LOGGER.debug("Get event by id {}", id);
        return performanceRepository.findOneById(id);
    }

    @Override
    public List<Performance> findAll(Pageable pageable) {
        LOGGER.trace("findAllPerformances()");
        return performanceRepository.findAll(pageable).getContent();
    }

    @Override
    public Performance addEvent(Performance performance) {
        LOGGER.trace("addEvent({})", performance);
        return performanceRepository.save(performance);
    }

    @Override
    public List<Performance> search(PerformanceSearch performance, Pageable pageable) {
        LOGGER.trace("searchPerformance({})", performance);

        PerformanceSpecificationBuilder builder = new PerformanceSpecificationBuilder();

        if (performance.getDate() != null) {
            builder.with("date", "+", performance.getDate());
        }

        if (performance.getEventId() != null) {
            Event event = eventRepository.findOneById(performance.getEventId());
            List<Performance> retList = new ArrayList<>();

            if (event != null) {
                List<Performance> performances = performanceRepository.findAll(builder.build(), pageable).getContent();

                for (Performance p : performances) {
                    for (Performance p2 : event.getPerformances()) {
                        if (p.equals(p2)) {
                            retList.add(p2);
                        }
                    }
                }
            }

            return retList;

        }

        return performanceRepository.findAll(builder.build(), pageable).getContent();
    }

    @Override
    public List<Performance> findAllPerformancesByLocation(Long addressId, Pageable pageable) {
        return performanceRepository.findAllByLocation_Id(addressId, pageable).getContent();
    }

    @Override
    public List<Performance> findAllPerformancesByArtist(Long artistId, Pageable pageable) {
        return performanceRepository.findAllByArtistId(artistId, pageable).getContent();
    }
}