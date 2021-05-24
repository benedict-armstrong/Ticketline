package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PaginationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.AddressMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaginationMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.AddressService;
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
@RequestMapping(value = "/api/v1/addresses")
public class AddressEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final PaginationMapper paginationMapper;

    @Autowired
    public AddressEndpoint(AddressService addressService, AddressMapper addressMapper, PaginationMapper paginationMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        this.paginationMapper = paginationMapper;
    }

    @PermitAll
    @GetMapping
    @Operation(summary = "Get all performance locations")
    public List<AddressDto> findAllByCriteria(PaginationDto paginationDto) {
        LOGGER.info("GET /api/v1/addresses");
        return addressMapper.addressListToAddressListDto(addressService.findAllByCriteria(paginationMapper.paginationDtoToPageable(paginationDto)));
    }

    @Secured("ROLE_ORGANIZER")
    @PostMapping
    @Operation(summary = "Add a new address")
    public AddressDto addAddress(@Valid @RequestBody AddressDto addressDto) {
        LOGGER.info("POST /api/v1/addresses body: {}", addressDto);
        return addressMapper.addressToAddressDto(addressService.addAddress(addressMapper.addressDtoToAddress(addressDto)));
    }
}