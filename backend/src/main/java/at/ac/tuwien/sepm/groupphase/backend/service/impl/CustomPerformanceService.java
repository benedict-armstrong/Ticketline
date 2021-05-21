package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
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
    private final ArtistRepository artistRepository;

    @Autowired
    public CustomPerformanceService(PerformanceRepository performanceRepository, ArtistRepository artistRepository) {
        this.performanceRepository = performanceRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Performance> findAllByDate(Pageable pageable) {
        LOGGER.debug("Get all events");
        return performanceRepository.findAllByOrderByDateAsc(pageable).getContent();
    }

    @Override
    public Performance findById(long id) {
        LOGGER.debug("Get event by id {}", id);
        return performanceRepository.findOneById(id);
    }

    @Override
    public Performance addEvent(Performance performance) {
        LOGGER.trace("addEvent({})", performance);

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

        return performanceRepository.save(performance);
    }
}