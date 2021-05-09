package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomNewsService implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsRepository newsRepository;

    @Autowired
    public CustomNewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getAll(Long limit, Long offset) {
        LOGGER.trace("Get all news");
        return newsRepository.getAll(limit, offset);
    }

    @Override
    public News addNews(News news) {
        LOGGER.debug("Add new news");
        LOGGER.info(news.getCustomImages().toString());
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Override
    public News getOneById(Long id) {
        LOGGER.debug("Get one news by id");

        News news = newsRepository.findOneById(id);

        if (news != null) {
            return news;
        }

        throw new NotFoundException(String.format("Could not find the news with the id %d", id));
    }
}
