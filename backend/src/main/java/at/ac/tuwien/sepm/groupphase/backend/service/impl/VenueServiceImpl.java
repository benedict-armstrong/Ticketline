package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final SectorRepository sectorRepository;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository, SectorRepository sectorRepository) {
        this.venueRepository = venueRepository;
        this.sectorRepository = sectorRepository;
    }

    @Override
    public Venue add(Venue venue) {

        venue.getSectors().forEach(sector -> sector.setId(null));
        venue.setSectors(sectorRepository.saveAll(venue.getSectors()));

        for (LayoutUnit layoutUnit: venue.getLayout()) {
            Long localId = layoutUnit.getSector().getLocalId();
            layoutUnit.setSector(venue.getSectors().stream().filter(layoutUnitFilter -> localId.equals(layoutUnitFilter.getLocalId())).findFirst().orElse(null));
        }

        return venueRepository.save(venue);
    }
}
