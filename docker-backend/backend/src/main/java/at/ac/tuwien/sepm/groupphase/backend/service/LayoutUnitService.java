package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface LayoutUnitService {

    /**
     * Add new LayoutUnit to System.
     *
     * @param layoutUnit LayoutUnit to be persisted
     * @return the created layoutUnit
     */
    LayoutUnit add(LayoutUnit layoutUnit);

    /**
     * save multiple layoutUnits.
     *
     * @param layoutUnits to be persisted
     * @return the created layoutUnits
     */
    List<LayoutUnit> saveAll(Set<LayoutUnit> layoutUnits);

    /**
     * Returns the layoutUnit with the given id.
     *
     * @param id to find
     * @return the layoutUnit
     */
    LayoutUnit findById(Long id);

    /**
     * Find all layoutUnits in given Sector.
     *
     * @param sector of the layoutUnits to find
     * @return list of all found layoutUnits
     */
    List<LayoutUnit> findBySector(Sector sector);
}
