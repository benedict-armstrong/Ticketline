package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MapperTest implements TestDataMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    private SectorTypeMapper sectorTypeMapper;

    @Autowired
    private UserMapper userMapper;


    @Test
    @DisplayName("Testing address mapper")
    public void addressMapper() {
        assertEquals(ADDRESS_ENTITY, addressMapper.addressDtoToAddress(ADDRESS_DTO));
        assertEquals(ADDRESS_DTO, addressMapper.addressToAddressDto(ADDRESS_ENTITY));
    }

    @Test
    @DisplayName("Testing address mapper special functions")
    public void addressMapperSpecialCases() {
        List<AddressDto> dtoList = new ArrayList<>();
        dtoList.add(ADDRESS_DTO);
        dtoList.add(ADDRESS2_DTO);

        List<Address> entityList = new ArrayList<>();
        entityList.add(ADDRESS_ENTITY);
        entityList.add(ADDRESS2_ENTITY);

        assertEquals(dtoList, addressMapper.addressListToAddressListDto(entityList));
    }

    @Test
    @DisplayName("Testing artist mapper")
    public void artistMapper() {
        assertEquals(ARTIST_ENTITY, artistMapper.artistDtoToArtist(ARTIST_DTO));
        assertEquals(ARTIST_DTO, artistMapper.artistToArtistDto(ARTIST_ENTITY));
    }

    @Test
    @DisplayName("Testing artist mapper special functions")
    public void artistMapperSpecialCases() {
        List<ArtistDto> dtoList = new ArrayList<>();
        dtoList.add(ARTIST_DTO);
        dtoList.add(ARTIST2_DTO);

        List<Artist> entityList = new ArrayList<>();
        entityList.add(ARTIST_ENTITY);
        entityList.add(ARTIST2_ENTITY);

        assertEquals(dtoList, artistMapper.artistListToArtistDtoList(entityList));
    }

    @Test
    @DisplayName("Testing event mapper")
    public void eventMapper() {
        assertEquals(EVENT_ENTITY, eventMapper.eventDtoToEvent(EVENT_DTO));
        assertEquals(EVENT_DTO, eventMapper.eventToEventDto(EVENT_ENTITY));
    }

    @Test
    @DisplayName("Testing event mapper special functions")
    public void eventMapperSpecialCases() {
        List<EventDto> dtoList = new ArrayList<>();
        dtoList.add(EVENT_DTO);
        dtoList.add(EVENT2_DTO);

        List<Event> entityList = new ArrayList<>();
        entityList.add(EVENT_ENTITY);
        entityList.add(EVENT2_ENTITY);

        assertEquals(dtoList, eventMapper.eventListToEventDtoList(entityList));
    }

    @Test
    @DisplayName("Testing user mapper")
    public void userMapper() {
        assertEquals(USER_ENTITY, userMapper.userDtoToApplicationUser(USER_DTO));
        assertEquals(USER_DTO, userMapper.applicationUserToUserDto(USER_ENTITY));
    }
}
