package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;


@Service
public class VenueServiceImpl implements VenueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final VenueRepository venueRepository;
    private final SectorService sectorService;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository, SectorService sectorService, AuthenticationFacade authenticationFacade, UserService userService) {
        this.venueRepository = venueRepository;
        this.sectorService = sectorService;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Venue add(Venue venue) {
        LOGGER.trace("add({})", venue);
        venue.getSectors().forEach(sector -> sector.setId(null));
        venue.setSectors(sectorService.saveAll(venue.getSectors()));

        for (LayoutUnit layoutUnit : venue.getLayout()) {
            Long localSectorId = layoutUnit.getSector().getLocalId();
            layoutUnit.setSector(
                venue.getSectors().stream().filter(
                    sector ->
                        localSectorId.equals(sector.getLocalId())
                ).findFirst().orElse(null)
            );
        }

        ApplicationUser user = userService.findApplicationUserByEmail(authenticationFacade.getMail(), false);

        venue.setOwner(user);

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

    @Override
    public List<Venue> getAll() {

        /*ApplicationUser user = userService.findUserByEmail(authenticationFacade.getMail());
        LOGGER.trace("getAll({})", user.getEmail());

        if (authenticationFacade.isAdmin()) {
            return venueRepository.findAll();
        } else {
            return venueRepository.findAllByOwnerIs(user);
        }*/

        return venueRepository.findAll();
    }
}
