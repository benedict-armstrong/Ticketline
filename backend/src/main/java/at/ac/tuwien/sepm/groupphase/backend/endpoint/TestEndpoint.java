package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/v1/test")
public class TestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public TestEndpoint() {
    }

    @PermitAll
    @GetMapping
    @Operation(summary = "Get a specific address")
    public void getOneById() {
        LOGGER.info("GET /api/v1/test");
        Address address = Address.builder().name("Max Mustermann").lineOne("Teststraße 1").city("Wien").postcode("1010").country("Österreich").build();
        ApplicationUser user = ApplicationUser.builder()
            .firstName("Max")
            .lastName("Mustermann")
            .email("admin@email.com")
            .lastLogin(LocalDateTime.now())
            .role(ApplicationUser.UserRole.ADMIN)
            .status(ApplicationUser.UserStatus.ACTIVE)
            .password("ADMIN_PASSWORD")
            .points(0)
            .address(address)
            .telephoneNumber("+43 660 123456789")
            .build();

        PdfService pdf = new PdfService(user);
    }
}