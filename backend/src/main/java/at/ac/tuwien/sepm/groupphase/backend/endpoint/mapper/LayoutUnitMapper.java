package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LayoutUnitDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import org.mapstruct.Mapper;

@Mapper
public interface LayoutUnitMapper {

    LayoutUnitDto layoutUnitToLayoutUnitDto(LayoutUnit layoutUnit);

    LayoutUnit layoutUnitDTOToLayoutUnit(LayoutUnitDto layoutUnitDto);

}


