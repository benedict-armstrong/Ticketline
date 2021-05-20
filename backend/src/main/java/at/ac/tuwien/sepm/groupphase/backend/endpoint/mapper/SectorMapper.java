package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.mapstruct.Mapper;

@Mapper
public interface SectorMapper {

    SectorDto sectorToSectorDto(Sector sector);

    Sector sectorDtoToSector(SectorDto sectorDto);

}
