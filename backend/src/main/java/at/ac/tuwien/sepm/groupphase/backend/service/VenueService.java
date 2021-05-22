package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;

public interface VenueService {

    /**
     * Add new Venue to System.
     *
     * @param venue Venue to be persisted
     * @return the created Venue
     */
    Venue add(Venue venue);


    /**
     * Find Venue by Id.
     *
     * @param id Id of Venue to find
     * @return Venue with given Id
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException when no Venue with given Id is found.
     */
    Venue getOneById(Long id);
}
