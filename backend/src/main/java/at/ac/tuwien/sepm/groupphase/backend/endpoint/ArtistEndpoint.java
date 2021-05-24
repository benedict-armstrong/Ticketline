package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaginationMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/artists")
public class ArtistEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtistService artistService;
    private final ArtistMapper artistMapper;
    private final PaginationMapper paginationMapper;

    @Autowired
    public ArtistEndpoint(ArtistService artistService, ArtistMapper artistMapper, PaginationMapper paginationMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
        this.paginationMapper = paginationMapper;
    }

    @PermitAll
    @GetMapping
    @Operation(summary = "Get all artists")
    public List<ArtistDto> findAllByCriteria(PaginationDto paginationDto) {
        LOGGER.info("GET /api/v1/artists");
        return artistMapper.artistListToArtistDtoList(artistService.findAllByCriteria(paginationMapper.paginationDtoToPageable(paginationDto)));
    }

    @Secured("ROLE_ORGANIZER")
    @PostMapping
    @Operation(summary = "Add a new artist")
    public ArtistDto addArtist(@Valid @RequestBody ArtistDto artistDto) {
        LOGGER.info("POST /api/v1/artists body: {}", artistDto);
        return artistMapper.artistToArtistDto(artistService.addArtist(artistMapper.artistDtoToArtist(artistDto)));
    }
}