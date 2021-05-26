package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSearch;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
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


        return performanceRepository.findAll(builder.build(), pageable).getContent();
    }
}