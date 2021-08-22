package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatCountDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatCount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SeatCountMapper extends FileTypeMapper {
    SeatCountDto seatCountToSeatCountDto(SeatCount seatCount);

    List<SeatCountDto> seatCountListToSeatCountDtoList(List<SeatCount> seatCounts);
}
