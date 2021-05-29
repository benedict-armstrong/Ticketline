package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
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

    @Autowired
    public TicketEndpoint(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/cart")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user cart")
    public List<TicketDto> getCartTickets() {
        LOGGER.info("GET /api/v1/tickets/cart");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.IN_CART));
    }

    @PostMapping("/cart")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket in cart of user")
    public TicketDto createCartTicket(@RequestBody TicketDto ticket) {
        LOGGER.info("POST /api/v1/tickets/cart {}", ticket);
        return ticketMapper.ticketToTicketDto(ticketService.save(
            ticketMapper.ticketDtoToTicket(ticket), Ticket.Status.IN_CART
        ));
    }

    @PutMapping("/amount")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a tickets seats")
    public TicketUpdateDto updateAmount(@RequestBody TicketUpdateDto ticketUpdate) {
        LOGGER.info("PUT /api/v1/tickets/seats {}", ticketUpdate);
        return ticketService.updateSeats(ticketUpdate);
    }

    @PutMapping("/checkout")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buys ticket from cart")
    public boolean checkout() {
        LOGGER.info("PUT /api/v1/tickets/checkout");
        return ticketService.checkout();
    }

    @PutMapping(path = "/{id}/cancel")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancel a ticket")
    public TicketDto cancelTicket(@PathVariable Long id) {
        LOGGER.info("POST /api/v1/tickets/{}/cancel", id);
        return ticketMapper.ticketToTicketDto(ticketService.cancel(id));
    }

    @DeleteMapping(path = "/{id}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete ticket")
    public boolean delete(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/tickets/{}", id);
        return ticketService.delete(id);
    }

    @GetMapping("/paid")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user that have been paid for")
    public List<TicketDto> getPaidTickets() {
        LOGGER.info("GET /api/v1/tickets/paid");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.PAID_FOR));
    }
}
