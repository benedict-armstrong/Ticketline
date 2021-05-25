package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.Arrays;

public interface TestDataTicket extends TestData {

    String TICKET_BASE_URI = BASE_URI + "/tickets";

    TicketType STANDARD_TICKET_TYPE = TicketType.builder()
        .title("Standard")
        .multiplier(1.0)
        .build();
    TicketType VIP_TICKET_TYPE = TicketType.builder()
        .title("VIP")
        .multiplier(2.33)
        .build();
    TicketType DISCOUNT_TICKET_TYPE = TicketType.builder()
        .title("Discount")
        .multiplier(0.85)
        .build();

    Ticket STANDARD_TICKET = Ticket.builder()
        .seats(Arrays.asList(1L, 5L, 6L))
        .ticketType(STANDARD_TICKET_TYPE)
        .build();
    Ticket VIP_TICKET = Ticket.builder()
        .seats(Arrays.asList(1L, 5L, 6L))
        .ticketType(VIP_TICKET_TYPE)
        .build();
    Ticket DISCOUNT_TICKET = Ticket.builder()
        .seats(Arrays.asList(1L, 5L, 6L))
        .ticketType(DISCOUNT_TICKET_TYPE)
        .build();

}
