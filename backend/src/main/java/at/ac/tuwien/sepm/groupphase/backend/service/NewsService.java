package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;

import java.util.List;

public interface NewsService {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @param limit the amount of news entries to be returned
     * @param offset the maximum ID that the news must have
     * @return ordered list of all satisfying message entries
     */
    List<News> getAll(Long limit, Long offset);

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
     */
    News getOneById(Long id);


}
