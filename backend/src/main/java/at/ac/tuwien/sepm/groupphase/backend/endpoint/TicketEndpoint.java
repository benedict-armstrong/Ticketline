package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketStatusMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/tickets")
public class TicketEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final TicketStatusMapper ticketStatusMapper;

    @Autowired
    public TicketEndpoint(TicketService ticketService, TicketMapper ticketMapper,
                          TicketStatusMapper ticketStatusMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.ticketStatusMapper = ticketStatusMapper;
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket")
    public TicketDto createTicket(@RequestBody TicketDto ticket, @RequestParam(name = "mode") String mode) {
        LOGGER.info("POST /api/v1/tickets {}", ticket);
        return ticketMapper.ticketToTicketDto(ticketService.save(
            ticketMapper.ticketDtoToTicket(ticket), ticketStatusMapper.modeToStatus(mode)
        ));
    }

    @PutMapping(path = "/{id}")
    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cancel a ticket")
    public TicketDto cancelTicket(@PathVariable Long id) {
        LOGGER.info("POST /api/v1/tickets/{}", id);
        return ticketMapper.ticketToTicketDto(ticketService.cancel(id));
    }

}
