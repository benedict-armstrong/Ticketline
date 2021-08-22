package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>, JpaSpecificationExecutor<Performance> {

    /**
     * Find one performance by id.
     *
     * @param id of the performance to find
     * @return performance with the id.
     */
    Performance findOneById(Long id);

    /**
     * Finds all performances to be pruned.
     *
     * @param pruneDate Date to cut
     * @return list of pruned performances
     */
    List<Performance> findAllByDateBetween(LocalDateTime pruneDate, LocalDateTime pruneDate1);

    /**
     * find all performances for one artist.
     *
     * @param id of the artist of the performances
     * @param pageable pagination
     * @return all performances by this artist
     */
    Page<Performance> findAllByArtistId(Long id, Pageable pageable);

    /**
     * find all performances for one location.
     *
     * @param id of the location of the performances
     * @param pageable pagination
     * @return all performances by this location
     */
    Page<Performance> findAllByVenue_Address_Id(Long id, Pageable pageable);
}
