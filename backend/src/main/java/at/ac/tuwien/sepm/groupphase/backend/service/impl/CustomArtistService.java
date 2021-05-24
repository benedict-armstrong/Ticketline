package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
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
    @Transactional(readOnly = true)
    public List<Artist> findAllByCriteria(Pageable pageRequest) {
        return artistRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Artist addArtist(Artist artist) {
        return artistRepository.save(artist);
    }
}
