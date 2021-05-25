package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address addressDtoToAddress(AddressDto addressDto);

    @Mapping(target = "performances", source = "performances")
    AddressDto addressToAddressDto(Address address);

    @Mapping(target = "location", ignore = true)
    PerformanceDto eventToEventDto(Performance performance);

    @Mapping(target = "performances", ignore = true)
    ArtistDto artistToArtistDto(Artist artist);

    List<AddressDto> addressListToAddressListDto(List<Address> addressList);

}
