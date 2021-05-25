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
}
