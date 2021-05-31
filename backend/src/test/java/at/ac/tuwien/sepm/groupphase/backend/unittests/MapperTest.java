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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    private TicketTypeMapper ticketTypeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private SectorMapper sectorMapper;

    @Autowired
    private VenueMapper venueMapper;


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

    @Test
    @DisplayName("Testing mappers for null values")
    public void nullMapper() {
        assertNull(addressMapper.addressToAddressDto(null));
        assertNull(addressMapper.addressDtoToAddress(null));
        assertNull(addressMapper.addressListToAddressListDto(null));

        assertNull(artistMapper.artistToArtistDto(null));
        assertNull(artistMapper.artistDtoToArtist(null));
        assertNull(artistMapper.artistListToArtistDtoList(null));

        assertNull(eventMapper.eventToEventDto(null));
        assertNull(eventMapper.eventDtoToEvent(null));
        assertNull(eventMapper.eventListToEventDtoList(null));

        assertNull(userMapper.applicationUserToUserDto(null));
        assertNull(userMapper.userDtoToApplicationUser(null));

        assertNull(ticketMapper.ticketToTicketDto(null));
        assertNull(ticketMapper.ticketDtoToTicket(null));
        assertNull(ticketMapper.ticketListToTicketDtoList(null));
        assertNull(ticketMapper.ticketDtoListToTicketList(null));
        assertNull(ticketMapper.ticketDtoArrayToTicketSet (null));
        assertNull(ticketMapper.ticketSetToTicketDtoArray(null));

        assertNull(ticketTypeMapper.ticketTypeToTicketTypeDto(null));
        assertNull(ticketTypeMapper.ticketTypeDtoToTicketType(null));
        assertNull(ticketTypeMapper.ticketTypeSetToTicketTypeDtoArray(null));
        assertNull(ticketTypeMapper.ticketTypeDtoArrayToTicketTypeSet(null));

        assertNull(sectorTypeMapper.sectorTypeToSectorTypeDto(null));
        assertNull(sectorTypeMapper.sectorTypeDtoToSectorType(null));
        assertNull(sectorTypeMapper.sectorTypeSetToSectorTypeDtoArray(null));
        assertNull(sectorTypeMapper.sectorTypeDtoArrayToSectorTypeSet(null));

        assertNull(bookingMapper.bookingDtoToBooking(null));
        assertNull(bookingMapper.bookingToBookingDto(null));
        assertNull(bookingMapper.bookingListToBookingDtoList(null));

        assertNull(newsMapper.newsDtoToNews(null));
        assertNull(newsMapper.newsToNewsDto(null));
        assertNull(newsMapper.newsListToNewsDtoList(null));

        assertNull(performanceMapper.performanceToPerformanceDto(null));
        assertNull(performanceMapper.performanceDtoToPerformance(null));
        assertNull(performanceMapper.performanceListToPerformanceDtoList(null));
        assertNull(performanceMapper.performanceDtoArrayToPerformanceSet(null));
        assertNull(performanceMapper.performanceSetToPerformanceDtoArray(null));
        assertNull(performanceMapper.performanceSearchDtoToPerformanceSearch(null));

        assertNull(sectorMapper.sectorToSectorDto(null));
        assertNull(sectorMapper.sectorDtoToSector(null));
        assertNull(sectorMapper.sectorListToSectorDtoList(null));
        assertNull(sectorMapper.sectorDtoListToSectorList(null));

        assertNull(venueMapper.venueToVenueDto((Venue) null));
        assertNull(venueMapper.venueToVenueDto((List<Venue>) null));
        assertNull(venueMapper.venueDtoToVenue(null));
    }
}
