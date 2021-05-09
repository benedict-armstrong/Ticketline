package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidQueryParameterException;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @Autowired
    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    //@Secured("ROLE_ADMIN")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Publish a new news")
    public NewsDto create(@Valid @RequestBody NewsDto newsDto) {
        LOGGER.info("POST /api/v1/news body: {}", newsDto);
        return newsMapper.newsToNewsDto(newsService.addNews(newsMapper.newsDtoToNews(newsDto)));
    }

    @PermitAll
    @GetMapping
    @Operation(summary = "Get all news")
    public List<NewsDto> getAll(@RequestParam Map<String, String> params) {
        LOGGER.info("GET /api/v1/news?" + params);
        long limit = 8L;
        long offset = Long.MAX_VALUE;
        try {
            if (params.containsKey("limit")) {
                limit = Long.parseLong(params.get("limit"));
            }
            if (params.containsKey("offset")) {
                offset = Long.parseLong(params.get("offset"));
            }
        } catch (NumberFormatException e) {
            throw new InvalidQueryParameterException("Query parameters limit and offset must be numeric", e);
        }
        return newsService.getAll(limit, offset).stream().map(newsMapper::newsToNewsDto).collect(Collectors.toList());
    }

}
