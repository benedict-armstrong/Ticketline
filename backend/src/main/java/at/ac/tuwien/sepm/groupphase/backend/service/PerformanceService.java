package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerformanceService {

    /**
     * Find performance by id.
     *
     * @return performance with id
     */
    Performance findById(long id);

    /**
     * add a new performance.
     *
     * @param performance to be added
     * @return the added performance with id set
     */
    Performance addEvent(Performance performance);

    /**
     * find all performances for one artist or location.
     *
     * @param addressId if of the location
     * @param pageable pagination
     * @return all performances of this artist or location
     */
    List<Performance> findAllPerformancesByLocation(Long addressId, Pageable pageable);

    /**
     * find all performances for one artist.
     *
     * @param artistId id of the artist
     * @return all performances of this artist
     */
    List<Performance> findAllPerformancesByArtist(Long artistId, Pageable pageable);
}
