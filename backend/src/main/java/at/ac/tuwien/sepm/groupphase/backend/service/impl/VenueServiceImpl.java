package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;


@Service
public class VenueServiceImpl implements VenueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final VenueRepository venueRepository;
    private final SectorRepository sectorRepository;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository, SectorRepository sectorRepository) {
        this.venueRepository = venueRepository;
        this.sectorRepository = sectorRepository;
    }

    @Override
    public Venue add(Venue venue) {
        LOGGER.trace("add({})", venue);
        venue.getSectors().forEach(sector -> sector.setId(null));
        venue.setSectors(sectorRepository.saveAll(venue.getSectors()));

        for (LayoutUnit layoutUnit: venue.getLayout()) {
            Long localId = layoutUnit.getSector().getLocalId();
            layoutUnit.setSector(venue.getSectors().stream().filter(layoutUnitFilter -> localId.equals(layoutUnitFilter.getLocalId())).findFirst().orElse(null));
        }

        return venueRepository.save(venue);
    }

    @Override
    public Venue getOneById(Long id) {
        LOGGER.trace("getOneById({})", id);
        Optional<Venue> optionalVenue = venueRepository.findById(id);
        if (optionalVenue.isPresent()) {
            Venue venue = optionalVenue.get();

            Hibernate.initialize(venue);

            return venue;
        }
        throw new NotFoundException(String.format("Could not find venue with id: %d", id));
    }
}
