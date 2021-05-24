package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AddressMapper {

    Address addressDtoToAddress(AddressDto addressDto);

    @Mapping(target = "performanceDtos", source = "performances")
    AddressDto addressToAddressDto(Address address);

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "artist", ignore = true)
    PerformanceDto eventToEventDto(Performance performance);

    List<AddressDto> addressListToAddressListDto(List<Address> addressList);

}
