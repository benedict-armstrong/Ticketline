package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
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
@RequestMapping(value = "/api/v1/venues")
public class VenueEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final VenueService venueService;
    private final VenueMapper venueMapper;

    @Autowired
    public VenueEndpoint(VenueService venueService, VenueMapper venueMapper) {
        this.venueService = venueService;
        this.venueMapper = venueMapper;
    }

    @Secured("ROLE_ORGANIZER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new venue")
    public VenueDto create(@Valid @RequestBody VenueDto venueDto) {
        LOGGER.info("POST /api/v1/venue Name: {}, Sectors: {}", venueDto.getName(), venueDto.getSectors());
        return venueMapper.venueToVenueDto(venueService.add(venueMapper.venueDtoToVenue(venueDto)));
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all venues")
    public List<VenueDto> getAll() {
        LOGGER.info("GET /api/v1/venues/");
        return venueMapper.venueToVenueDto(venueService.getAll());
    }

    @GetMapping(value = {"/{id}"})
    @PermitAll
    @Operation(summary = "Get a venue by id")
    public VenueDto getOneById(@Valid @PathVariable("id") Long id) {
        LOGGER.info("GET /api/v1/venue/{}", id);
        return venueMapper.venueToVenueDto(venueService.getOneById(id));
    }

}
