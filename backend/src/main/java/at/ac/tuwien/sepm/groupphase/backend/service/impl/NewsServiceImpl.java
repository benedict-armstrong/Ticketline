package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getAll(Pageable pageRequest) {
        LOGGER.trace("getAll({})", pageRequest);
        return newsRepository.findAllByOrderByPublishedAtDesc(pageRequest).getContent();
    }

    @Override
    public News addNews(News news) {
        LOGGER.trace("addNews({})", news);
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Override
    public News getOneById(Long id) throws NotFoundException {
        LOGGER.trace("getOneById({})", id);
        News news = newsRepository.findOneById(id);
        if (news != null) {
            return news;
        }
        throw new NotFoundException(String.format("Could not find the news with the id %d", id));
    }
}
