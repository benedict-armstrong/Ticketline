package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface SectorTypeMapper {
    SectorTypeDto sectorTypeToSectorTypeDto(SectorType sectorType);

    SectorType sectorTypeDtoToSectorType(SectorTypeDto sectorTypeDto);

    SectorTypeDto[] sectorTypeSetToSectorTypeDtoArray(Set<SectorType> sectorTypeSet);

    Set<SectorType> sectorTypeDtoArrayToSectorTypeSet(SectorTypeDto[] sectorTypeDtos);
}
