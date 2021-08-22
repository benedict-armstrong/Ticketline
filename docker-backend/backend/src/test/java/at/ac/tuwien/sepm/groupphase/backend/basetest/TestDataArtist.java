package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

public interface TestDataArtist extends TestData {
    String TEST_ARTIST_FIRSTNAME = "Max";
    String TEST_ARTIST_FIRSTNAME2 = "Melina";
    String TEST_ARTIST_LASTNAME = "Musterkuenstler";

    String ARTIST_BASE_URI = BASE_URI + "/artists";

    Artist TEST_ARTIST = Artist.builder()
        .firstName(TEST_ARTIST_FIRSTNAME)
        .lastName(TEST_ARTIST_LASTNAME)
        .build();

    static Artist getArtist() {
        return Artist.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();
    }

    static ArtistDto getArtistDto() {
        return ArtistDto.builder().firstName(TEST_ARTIST_FIRSTNAME).lastName(TEST_ARTIST_LASTNAME).build();
    }

}
