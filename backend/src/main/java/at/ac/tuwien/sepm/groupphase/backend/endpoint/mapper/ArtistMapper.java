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
public interface ArtistMapper {

    @Mapping(target = "performances", source = "performances")
    ArtistDto artistToArtistDto(Artist artist);

    Artist artistDtoToArtist(ArtistDto artistDto);

    @Mapping(target = "artist", ignore = true)
    PerformanceDto eventToEventDto(Performance performance);

    @Mapping(target = "performances", ignore = true)
    AddressDto addressToAddressDto(Address address);

    List<ArtistDto> artistListToArtistDtoList(List<Artist> artist);
}
