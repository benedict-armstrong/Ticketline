package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataTicket;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataVenue;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class PerformanceRepositoryTest implements TestDataEvent {

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private VenueRepository venueRepository;

    private Performance performance;

    @BeforeEach
    public void beforeEach(){
        Artist artist = artistRepository.save(TestDataEvent.TEST_EVENT_ARTIST);

        Venue venue = TestDataVenue.getVenue();
        List<Sector> savedSectors = new ArrayList<>();
        for (Sector sector : venue.getSectors()) {
            savedSectors.add(sectorRepository.save(sector));
        }
        for (LayoutUnit layoutUnit : venue.getLayout()) {
            if (layoutUnit != null) {
                layoutUnit.setSector(savedSectors.get(0));
            }
        }
        Venue savedVenue = venueRepository.save(venue);

        performance = Performance.builder()
            .title(TestDataEvent.TEST_EVENT_TITLE)
            .description(TestDataEvent.TEST_EVENT_DESCRIPTION)
            .date(TestDataEvent.TEST_PERFORMANCE_DATE)
            .artist(artist)
            .ticketTypes(TestDataTicket.getTicketTypes())
            .venue(savedVenue)
            .build();
    }

    @AfterEach
    public void afterEach() {
        performanceRepository.deleteAll();
        artistRepository.deleteAll();
        venueRepository.deleteAll();
        sectorRepository.deleteAll();
    }

    @Test
    @DisplayName("Check if one item is added")
    public void givenNothing_whenSavePerformance_thenFindListWithOneElement() {
        performanceRepository.save(performance);
        assertAll(
            () -> assertEquals(1, performanceRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("Should return test event by id")
    public void givenNothing_whenSavePerformance_thenFindPerformanceById() {
        performanceRepository.save(performance);

        assertAll(
            () -> assertNotNull(performanceRepository.findById(performance.getId()))
        );
    }

    @Test
    @DisplayName("Should return null when searching for negative id")
    public void givenNothing_whenFindOneById_ShouldBeNull() {
        assertNull(performanceRepository.findOneById(-1L));
    }

}
