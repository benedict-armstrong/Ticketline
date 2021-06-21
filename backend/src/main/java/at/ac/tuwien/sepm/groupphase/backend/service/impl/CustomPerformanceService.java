package at.ac.tuwien.sepm.groupphase.backend.service.impl;

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
import java.util.List;

@Service
public class CustomPerformanceService implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PerformanceRepository performanceRepository;

    @Autowired
    public CustomPerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
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
    public Performance addPerformance(Performance performance) {
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
            builder.with("event", ":", performance.getEventId());
        }

        if (performance.getVenue() != null) {
            builder.with("venue", ":", performance.getVenue());
        }

        return performanceRepository.findAll(builder.build(), pageable).getContent();
    }

    @Override
    public List<Performance> findAllPerformancesByVenueAddress(Long addressId, Pageable pageable) {
        return performanceRepository.findAllByVenue_Address_Id(addressId, pageable).getContent();
    }

    @Override
    public List<Performance> findAllPerformancesByArtist(Long artistId, Pageable pageable) {
        return performanceRepository.findAllByArtistId(artistId, pageable).getContent();
    }
}