package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SectorMapper {

    SectorMapper INSTANCE = Mappers.getMapper(SectorMapper.class);

    List<Sector> sectorDtoListToSectorList(List<SectorDto> sectorDtoList);

    List<SectorDto> sectorListToSectorDtoList(List<Sector> sectorList);

    SectorDto sectorToSectorDto(Sector sector);

    @Mapping(target = "localId", source = "id")
    Sector sectorDtoToSector(SectorDto sectorDto);

}
