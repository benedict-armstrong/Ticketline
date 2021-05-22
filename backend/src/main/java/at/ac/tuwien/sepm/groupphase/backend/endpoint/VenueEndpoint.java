package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;

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
        LOGGER.info("POST /api/v1/venue body: {}", venueDto);
        return venueMapper.venueToVenueDto(venueService.add(venueMapper.venueDtoToVenue(venueDto)));
    }

    @GetMapping(value = {"/{id}"})
    @PermitAll
    @Operation(summary = "Get a venue by id")
    public VenueDto getOneById(@Valid @PathVariable("id") Long id) {
        LOGGER.info("GET /api/v1/venue/{}", id);
        return venueMapper.venueToVenueDto(venueService.getOneById(id));
    }

}
