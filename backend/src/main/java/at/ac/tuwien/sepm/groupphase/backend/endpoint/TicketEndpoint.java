package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tickets")
public class TicketEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final PerformanceMapper performanceMapper;
    private final TicketTypeMapper ticketTypeMapper;

    @Autowired
    public TicketEndpoint(TicketService ticketService, TicketMapper ticketMapper,
                          PerformanceMapper performanceMapper, TicketTypeMapper ticketTypeMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.performanceMapper = performanceMapper;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all ticket in cart from user")
    public List<TicketDto> getCartTickets() {
        LOGGER.info("GET /api/v1/tickets");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.IN_CART));
    }

    @PostMapping(path = "/{amount}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket in cart of user")
    public List<TicketDto> createTicket(@RequestBody NewTicketDto addTicketDto, @PathVariable int amount) {
        LOGGER.info("POST /api/v1/tickets/{} {}", amount, addTicketDto);
        return ticketMapper.ticketListToTicketDtoList(ticketService.save(
            performanceMapper.performanceDtoToPerformance(addTicketDto.getPerformance()),
            ticketTypeMapper.ticketTypeDtoToTicketType(addTicketDto.getTicketType()),
            Ticket.Status.IN_CART, amount
        ));
    }

    @PutMapping("/checkout")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buys tickets in cart")
    public boolean checkout() {
        LOGGER.info("PUT /api/v1/tickets/checkout");
        return ticketService.checkout();
    }

    @PutMapping("/reserve")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reserves tickets in cart")
    public boolean reserve() {
        LOGGER.info("PUT /api/v1/tickets/reserve");
        return ticketService.reserve();
    }

    @DeleteMapping(path = "/{id}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete ticket")
    public boolean delete(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/tickets/{}", id);
        return ticketService.delete(id);
    }

    @DeleteMapping()
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete tickets")
    public boolean delete(@RequestBody List<Long> ids) {
        LOGGER.info("DELETE /api/v1/tickets {}", ids);
        return ticketService.delete(ids);
    }

    @GetMapping("/paid")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user that have been paid for")
    public List<TicketDto> getPaidTickets() {
        LOGGER.info("GET /api/v1/tickets/paid");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.PAID_FOR));
    }

    @GetMapping("/reserved")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user that have been reserved")
    public List<TicketDto> getReservedTickets() {
        LOGGER.info("GET /api/v1/tickets/reserved");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.RESERVED));
    }

    @GetMapping("/cancelled")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user that have been cancelled")
    public List<TicketDto> getCancelledTickets() {
        LOGGER.info("GET /api/v1/tickets/cancelled");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.CANCELLED));
    }
}
