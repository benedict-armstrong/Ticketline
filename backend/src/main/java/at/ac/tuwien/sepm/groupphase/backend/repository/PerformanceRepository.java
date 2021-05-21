package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    /**
     * Find all performance entries.
     *
     * @return ordered list of all performances
     */
    Page<Performance> findAllByOrderByDateAsc(Pageable pageable);

    /**
     * Find one performance by id.
     *
     * @param id of the performance to find
     * @return performance with the id.
     */
    Performance findOneById(Long id);
}
