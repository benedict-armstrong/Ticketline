package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @param pageRequest a Pageable page request
     * @return ordered list of all satisfying message entries
     */
    List<News> getAll(Pageable pageRequest);

    /**
     * Adds a single news entry.
     *
     * @param news to publish
     * @return complete news entry
     */
    News addNews(News news);

    /**
     * Returns the news with the specific id.
     *
     * @param id of the news to get
     * @return complete news entry
     * @throws NotFoundException if no news article with given ID exists
     */
    News getOneById(Long id);


}
