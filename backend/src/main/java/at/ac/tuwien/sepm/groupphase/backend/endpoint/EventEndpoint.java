package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaginationMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
    private final PaginationMapper paginationMapper;

    @Autowired
    public EventEndpoint(EventService eventService, EventMapper eventMapper, PaginationMapper paginationMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.paginationMapper = paginationMapper;
    }

    @Secured("ROLE_ORGANIZER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new event")
    public EventDto create(@Valid @RequestBody EventDto eventDto) {
        LOGGER.info("POST /api/v1/events body: {}", eventDto);
        return eventMapper.eventToEventDto(eventService.addEvent(eventMapper.eventDtoToEvent(eventDto)));
    }

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

    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a specific event")
    public EventDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/events/{}", id);
        return eventMapper.eventToEventDto(eventService.findById(id));
    }
}