package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtistService {

    /**
     * find all artists.
     *
     * @param pageRequest the pagination
     * @return list of artists
     */
    List<Artist> findAll(Pageable pageRequest);

    /**
     * add a new artist.
     *
     * @param artist to be added
     * @return the added artist with id set
     */
    Artist addArtist(Artist artist);

    /**
     * Returns a list of artists which match the search values.
     *
     * @param artist with the values to search
     * @return list of artists with all possible values
     */
    List<Artist> search(Artist artist, Pageable pageable);
}
