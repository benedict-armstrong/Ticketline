package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class ArtistDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_ARTISTS_TO_GENERATE = 10;

    private final ArtistRepository artistRepository;

    public ArtistDataGenerator(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @PostConstruct
    private void generateArtists() {
        if (artistRepository.findAll().size() > 0) {
            LOGGER.debug("artists already generated");
        } else {
            LOGGER.debug("generating {} artists", NUMBER_OF_ARTISTS_TO_GENERATE);
            Faker faker = new Faker();

            for (int i = 0; i < NUMBER_OF_ARTISTS_TO_GENERATE; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();

                Artist artist = Artist.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();

                LOGGER.debug("saving artist {}", artist);
                artistRepository.save(artist);
            }
        }
    }
}
