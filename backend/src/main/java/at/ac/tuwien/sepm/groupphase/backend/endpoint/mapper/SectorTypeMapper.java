package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import org.mapstruct.Mapper;

@Mapper
public interface SectorTypeMapper {
    SectorTypeDto sectorTypeToSectorTypeDto(SectorType sectorType);

    SectorType sectorTypeDtoToSectorType(SectorTypeDto sectorTypeDto);
}
