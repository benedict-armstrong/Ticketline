package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
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
@RequestMapping(value = "/api/v1/events")
public class EventEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;

    @Autowired
    public EventEndpoint(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    //@Secured("ROLE_ADMIN")
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new event")
    public EventDto create(@Valid @RequestBody EventDto eventDto) {
        LOGGER.info("POST /api/v1/events body: {}", eventDto);
        return eventMapper.eventToEventDto(eventService.addEvent(eventMapper.eventDtoToEvent(eventDto)));
    }

    //@Secured("ROLE_USER")
    @PermitAll
    @GetMapping
    @Operation(summary = "Get all events")
    public List<EventDto> findAll() {
        LOGGER.info("GET /api/v1/events");
        return eventMapper.eventToEventDto(eventService.findAll());
    }

    //@Secured("ROLE_USER")
    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a specific event")
    public EventDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/events/{}", id);
        EventDto test = eventMapper.eventToEventDto(eventService.findById(id));
        LOGGER.info(test.toString());
        return test;
    }
}
