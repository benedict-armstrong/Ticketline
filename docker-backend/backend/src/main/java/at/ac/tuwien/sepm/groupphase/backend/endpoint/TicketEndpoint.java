package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatCountDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatCountMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketTypeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tickets")
public class TicketEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final TicketTypeMapper ticketTypeMapper;
    private final SeatCountMapper seatCountMapper;

    @Autowired
    public TicketEndpoint(TicketService ticketService, TicketMapper ticketMapper,
                          TicketTypeMapper ticketTypeMapper, SeatCountMapper seatCountMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.ticketTypeMapper = ticketTypeMapper;
        this.seatCountMapper = seatCountMapper;
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all ticket in cart from user")
    public List<TicketDto> getCartTickets() {
        LOGGER.info("GET /api/v1/tickets");
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTickets(Ticket.Status.IN_CART));
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket in cart of user")
    public List<TicketDto> createTicket(@RequestBody NewTicketDto addTicketDto) {
        LOGGER.info("POST /api/v1/tickets {}", addTicketDto);
        if (addTicketDto.getSeatId() == null) {
            return ticketMapper.ticketListToTicketDtoList(ticketService.createTicketsByAmount(
                addTicketDto.getPerformanceId(),
                ticketTypeMapper.ticketTypeDtoToTicketType(addTicketDto.getTicketType()),
                Ticket.Status.IN_CART, addTicketDto.getAmount()
            ));
        }
        return ticketMapper.ticketListToTicketDtoList(ticketService.createTicketBySeat(
            addTicketDto.getPerformanceId(),
            ticketTypeMapper.ticketTypeDtoToTicketType(addTicketDto.getTicketType()),
            Ticket.Status.IN_CART, addTicketDto.getSeatId()
        ));
    }

    @PutMapping("/checkout")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buy tickets in cart")
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

    @PutMapping(path = "/update")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update ticket in cart")
    public TicketDto update(@Valid @RequestBody TicketDto ticketDto) {
        LOGGER.info("PUT /api/v1/tickets/update, {}", ticketDto);
        return ticketMapper.ticketToTicketDto(ticketService.updateTicket(ticketMapper.ticketDtoToTicket(ticketDto)));
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

    @GetMapping("/sales")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get ticket sales for past 7 days")
    public List<Double> getSales() {
        LOGGER.info("GET /api/v1/tickets/sales");
        return ticketService.getRelativeTicketSalesPastSevenDays();
    }

    @GetMapping("/pdf/{id}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get ticket pdf for performance")
    public File getTicketPdf(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/tickets/pdf/{}", id);
        return ticketService.getPdf(id);
    }

    @GetMapping("/confirmation")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get ticket dtos for performance")
    public List<TicketDto> getTicketDto(@RequestParam Long user, @RequestParam Long performance) {
        LOGGER.info("GET /api/v1/tickets/confirmation/{}/{}", user, performance);
        return ticketMapper.ticketListToTicketDtoList(ticketService.getTicketsForPerformance(performance, user));
    }

    @GetMapping("/{performanceId}/seatCounts")
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tickets from user that have been reserved")
    public List<SeatCountDto> getSeatCountsByPerformance(@PathVariable Long performanceId) {
        LOGGER.info("GET /api/v1/{}/seatCounts", performanceId);
        return  seatCountMapper.seatCountListToSeatCountDtoList(ticketService.getSeatCountsInPerformance(performanceId));
    }
}
