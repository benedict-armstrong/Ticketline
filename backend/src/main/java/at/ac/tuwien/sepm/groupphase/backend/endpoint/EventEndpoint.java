package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopEventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaginationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TopEventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopEvent;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(value = "/api/v1/events")
public class EventEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final TopEventMapper topEventMapper;
    private final PaginationMapper paginationMapper;

    @Autowired
    public EventEndpoint(EventService eventService, EventMapper eventMapper, PaginationMapper paginationMapper, TopEventMapper topEventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.paginationMapper = paginationMapper;
        this.topEventMapper = topEventMapper;
    }

    @Secured("ROLE_ORGANIZER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new event")
    public EventDto create(@Valid @RequestBody EventDto eventDto) {
        LOGGER.info("POST /api/v1/events body: {}", eventDto);
        return eventMapper.eventToEventDto(eventService.addEvent(eventMapper.eventDtoToEvent(eventDto)));
    }

    @Transactional
    @PermitAll
    @GetMapping
    @Operation(summary = "Get all events")
    public List<EventDto> findAll(PaginationDto paginationDto, EventDto event) {
        LOGGER.info("GET /api/v1/events");

        if (event != null) {
            return eventMapper.eventListToEventDtoList(eventService.search(eventMapper.eventDtoToEvent(event), paginationMapper.paginationDtoToPageable(paginationDto)));
        }

        return eventMapper.eventListToEventDtoList(eventService.findAllOrderedByStartDate(paginationMapper.paginationDtoToPageable(paginationDto)));
    }

    @Transactional
    @PermitAll
    @GetMapping(value = "/top")
    @Operation(summary = "Get top events")
    public List<TopEventDto> findTopEvents() {
        LOGGER.info("GET /api/v1/events/top");
        return topEventMapper.topEventListToTopEventDtoList(eventService.findTopEvents());
    }

    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a specific event")
    public EventDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/events/{}", id);
        return eventMapper.eventToEventDto(eventService.findById(id));
    }

    @PermitAll
    @GetMapping(value = "/search")
    @Operation(summary = "Get events by full text search")
    @Transactional
    public List<EventDto> search(@RequestParam String text, PaginationDto paginationDto) {
        LOGGER.info("GET /api/v1/events/search");

        return eventMapper.eventListToEventDtoList(eventService.search(text, paginationMapper.paginationDtoToPageable(paginationDto)));
    }

}