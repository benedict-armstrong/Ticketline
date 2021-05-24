package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.specification.ArtistSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomArtistService implements ArtistService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtistRepository artistRepository;

    public CustomArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }


    @Override
    public List<Artist> findAll(Pageable pageRequest) {
        return artistRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Artist addArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    public List<Artist> search(Artist artist, Pageable pageable) {
        LOGGER.trace("searchArtist({}, {})", artist.getFirstName(), artist.getLastName());

        ArtistSpecificationBuilder builder = new ArtistSpecificationBuilder();

        if (artist.getFirstName() != null) {
            builder.with("firstName", ":", artist.getFirstName());
        }
        if (artist.getLastName() != null) {
            builder.with("lastName", ":", artist.getLastName());
        }

        return artistRepository.findAll(builder.build(), pageable).getContent();
    }
}
