package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSearch;
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
     * Find all performances with pagination.
     *
     * @return list of all performances
     */
    List<Performance> findAll(Pageable pageable);

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

    /**
     * Returns a list of performances which match the search values.
     *
     * @param performance with the values to search
     * @return list of performances with all possible values
     */
    List<Performance> search(PerformanceSearch performance, Pageable pageable);
}
