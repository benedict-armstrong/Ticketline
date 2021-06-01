package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSearch;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(uses = {TicketTypeMapper.class, ArtistMapper.class, AddressMapper.class, VenueMapper.class, LayoutUnitMapper.class})
public interface PerformanceMapper extends FileTypeMapper {

    Performance performanceDtoToPerformance(PerformanceDto performanceDto);

    PerformanceDto performanceToPerformanceDto(Performance performance);

    List<PerformanceDto> performanceListToPerformanceDtoList(List<Performance> performances);

    PerformanceSearch performanceSearchDtoToPerformanceSearch(PerformanceSearchDto performanceSearchDto);

    Set<Performance> performanceDtoArrayToPerformanceSet(PerformanceDto[] performanceDtoArray);

    PerformanceDto[] performanceSetToPerformanceDtoArray(Set<Performance> set);
}
