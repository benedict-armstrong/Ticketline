package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.*;

public interface TestDataTicket extends TestData {

    String TICKET_BASE_URI = BASE_URI + "/tickets";

    List<Long> TICKET_SEATS = new ArrayList<>(
        Arrays.asList(10L)
    );

    TicketType STANDARD_TICKET_TYPE = TicketType.builder()
        .title("Standard")
        .price(100L)
        .build();
    TicketType VIP_TICKET_TYPE = TicketType.builder()
        .title("VIP")
        .price(200L)
        .build();
    TicketType DISCOUNT_TICKET_TYPE = TicketType.builder()
        .title("Discount")
        .price(50L)
        .build();

    Ticket STANDARD_TICKET = Ticket.builder()
        .ticketType(STANDARD_TICKET_TYPE)
        .build();
    Ticket VIP_TICKET = Ticket.builder()
        .ticketType(VIP_TICKET_TYPE)
        .build();
    Ticket DISCOUNT_TICKET = Ticket.builder()
        .ticketType(DISCOUNT_TICKET_TYPE)
        .build();

    static Set<TicketType> getTicketTypes() {
        Set<TicketType> set = new HashSet<>();
        set.add(
            TicketType.builder()
                .title(STANDARD_TICKET_TYPE.getTitle())
                .price(STANDARD_TICKET_TYPE.getPrice())
                .build()
        );
        set.add(
            TicketType.builder()
                .title(VIP_TICKET_TYPE.getTitle())
                .price(VIP_TICKET_TYPE.getPrice())
                .build()
        );
        set.add(
            TicketType.builder()
                .title(DISCOUNT_TICKET_TYPE.getTitle())
                .price(DISCOUNT_TICKET_TYPE.getPrice())
                .build()
        );
        return set;
    }

}
