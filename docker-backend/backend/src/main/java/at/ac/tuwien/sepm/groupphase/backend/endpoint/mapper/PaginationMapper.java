package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidQueryParameterException;
import org.mapstruct.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Mapper
public interface PaginationMapper {

    default Pageable paginationDtoToPageable(PaginationDto paginationDto) {
        try {
            return PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        } catch (IllegalArgumentException e) {
            throw new InvalidQueryParameterException(e.getMessage(), e);
        }
    }

}
