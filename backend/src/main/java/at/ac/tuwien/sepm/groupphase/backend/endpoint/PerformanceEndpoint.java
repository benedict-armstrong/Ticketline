package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaginationMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
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

@RestController
@RequestMapping(value = "/api/v1/performances")
public class PerformanceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PerformanceService performanceService;
    private final PerformanceMapper performanceMapper;

    @Autowired
    public PerformanceEndpoint(PerformanceService performanceService, PerformanceMapper performanceMapper) {
        this.performanceService = performanceService;
        this.performanceMapper = performanceMapper;
    }

    @Secured("ROLE_ORGANIZER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new performance")
    public PerformanceDto create(@Valid @RequestBody PerformanceDto performanceDto) {
        LOGGER.info("POST /api/v1/events body: {}", performanceDto);
        return performanceMapper.performanceToPerformanceDto(performanceService.addEvent(performanceMapper.performanceDtoToPerformance(performanceDto)));
    }

    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a specific performance")
    public PerformanceDto findById(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/performances/{}", id);
        return performanceMapper.performanceToPerformanceDto(performanceService.findById(id));
    }
}