package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface TestDataTicket extends TestData {

    String TICKET_BASE_URI = BASE_URI + "/tickets";

    SectorType STANDARD_SECTOR_TYPE = SectorType.builder().name("Test").numberOfTickets(10).build();

    TicketType STANDARD_TICKET_TYPE = TicketType.builder()
        .title("Standard")
        .sectorType(STANDARD_SECTOR_TYPE)
        .price(1.0)
        .build();
    TicketType VIP_TICKET_TYPE = TicketType.builder()
        .title("VIP")
        .sectorType(STANDARD_SECTOR_TYPE)
        .price(2.33)
        .build();
    TicketType DISCOUNT_TICKET_TYPE = TicketType.builder()
        .title("Discount")
        .sectorType(STANDARD_SECTOR_TYPE)
        .price(0.85)
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

    static Set<TicketType> getTicketTypes() {
        Set<TicketType> set = new HashSet<>();
        set.add(STANDARD_TICKET_TYPE);
        set.add(VIP_TICKET_TYPE);
        set.add(DISCOUNT_TICKET_TYPE);
        return set;
    }

}
