package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;

import java.util.List;

public interface SectorService {

    /**
     * save multiple sectors
     *
     * @param sectors to be persisted
     * @return the created sectors
     */
    List<Sector> saveAll(List<Sector> sectors);
}
