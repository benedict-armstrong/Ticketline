package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PerformanceMapper extends FileTypeMapper {

    Performance performanceDtoToPerformance(PerformanceDto performanceDto);

    PerformanceDto performanceToPerformanceDto(Performance performance);

    List<PerformanceDto> performanceListToPerformanceDtoList(List<Performance> performance);

}
