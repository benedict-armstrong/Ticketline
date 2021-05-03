package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;

import java.util.List;

public interface NewsService {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @return ordered list of al message entries
     */
    List<News> findAll();



    /**
     * Adds a single news entry.
     *
     * @param news to publish
     * @return complete news entry
     */
    News addNews(News news);

}
