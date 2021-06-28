package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LayoutUnitRepository extends JpaRepository<LayoutUnit, Long> {

    /**
     * Find all layoutUnits in given Sector.
     *
     * @param sector of the layoutUnits to find
     * @return list of all found layoutUnits
     */
    List<LayoutUnit> findLayoutUnitBySector(Sector sector);
}
