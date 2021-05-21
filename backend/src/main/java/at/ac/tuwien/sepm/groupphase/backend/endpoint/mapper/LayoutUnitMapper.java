package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LayoutUnitDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = SectorMapper.class)
public interface LayoutUnitMapper {

    LayoutUnitMapper INSTANCE = Mappers.getMapper(LayoutUnitMapper.class);

    default List<LayoutUnit> layoutUnitDtoMatrixToList(List<List<LayoutUnitDto>> layoutUnitMatrix) {
        if (layoutUnitMatrix == null) {
            return null;
        }
        int counter = 0;
        List<LayoutUnit> layout = new ArrayList<>();
        for (List<LayoutUnitDto> row : layoutUnitMatrix) {
            for (LayoutUnitDto layoutUnitDto: row) {
                if (layoutUnitDto != null) {
                    layout.add(layoutUnitDTOToLayoutUnit(layoutUnitDto, counter));
                }
                counter++;
            }
        }
        return layout;
    }

    default List<List<LayoutUnitDto>> layoutUnitListToLayoutUnitDtoMatrix(List<LayoutUnit> list, int width) {
        if (list == null) {
            return null;
        }

        int height = list.get(list.size() - 1).getLocalId() / width;

        if (list.get(list.size() - 1).getLocalId() % width > 0) {
            height++;
        }

        int index = 0;
        List<List<LayoutUnitDto>> matrix = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            List<LayoutUnitDto> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                if ((i * width + j) == list.get(index).getLocalId()) {
                    row.add(layoutUnitToLayoutUnitDto(list.get(index)));
                    index++;
                } else {
                    row.add(null);
                }
            }
            matrix.add(row);
        }

        return matrix;
    }

    default LayoutUnitDto layoutUnitToLayoutUnitDto(LayoutUnit layoutUnit) {
        if (layoutUnit == null) {
            return null;
        }
        return LayoutUnitDto.builder()
            .id(layoutUnit.getId())
            .sector(SectorMapper.INSTANCE.sectorToSectorDto(layoutUnit.getSector()))
            .customLabel(layoutUnit.getCustomLabel())
            .build();
    }

    default LayoutUnit layoutUnitDTOToLayoutUnit(LayoutUnitDto layoutUnitDto, int localId) {
        if (layoutUnitDto == null) {
            return null;
        }
        return LayoutUnit.builder()
            .id(layoutUnitDto.getId())
            .sector(SectorMapper.INSTANCE.sectorDtoToSector(layoutUnitDto.getSector()))
            .customLabel(layoutUnitDto.getCustomLabel())
            .localId(localId)
            .build();
    }

}


