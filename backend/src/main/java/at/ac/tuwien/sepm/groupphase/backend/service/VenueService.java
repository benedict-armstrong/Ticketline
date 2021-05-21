package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;

public interface VenueService {

    /**
     * Add new Venue to System
     *
     * @param venue Venue to be persisted
     * @return the created Venue
     */
    Venue add(Venue venue);
}
