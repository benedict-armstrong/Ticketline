package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaginationMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final PaginationMapper paginationMapper;

    @Autowired
    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper, PaginationMapper paginationMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.paginationMapper = paginationMapper;
    }

    //@Secured("ROLE_ADMIN")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Publish a news article")
    public NewsDto create(@Valid @RequestBody NewsDto newsDto) {
        LOGGER.info("POST /api/v1/news body: {}", newsDto);
        return newsMapper.newsToNewsDto(newsService.addNews(newsMapper.newsDtoToNews(newsDto)));
    }

    @PermitAll
    @GetMapping
    @Operation(summary = "Get all news")
    public List<NewsDto> getAll(PaginationDto paginationDto) {
        LOGGER.info("GET /api/v1/news?{}", paginationDto);
        return newsMapper.newsListToNewsDtoList(
            newsService.getAll(paginationMapper.paginationDtoToPageable(paginationDto))
        );
    }

    @GetMapping(value = {"/{id}"})
    @PermitAll
    @Operation(summary = "Get a news article by id")
    public NewsDto getOneById(@Valid @PathVariable("id") Long id) {
        LOGGER.info("GET /api/v1/news/{}", id);
        return newsMapper.newsToNewsDto(newsService.getOneById(id));
    }

}
