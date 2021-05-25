package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataArtist;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataEvent;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.specification.ArtistSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class ArtistRepositoryTest implements TestDataArtist {
    @Autowired
    ArtistRepository artistRepository;

    private Artist artist;

    @BeforeEach
    public void beforeEach(){
        artist = Artist.builder()
            .firstName(TEST_ARTIST_FIRSTNAME)
            .lastName(TEST_ARTIST_LASTNAME)
            .build();
    }

    @AfterEach
    public void afterEach() {
        artistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return artist when searching for artist")
    public void givenArtist_whenSearch_ShouldReturnArtist() {
        artistRepository.save(artist);

        ArtistSpecificationBuilder builder = new ArtistSpecificationBuilder();
        builder.with("firstName", ":", artist.getFirstName());

        assertEquals(artist, artistRepository.findAll(builder.build()).get(0));
    }
}
