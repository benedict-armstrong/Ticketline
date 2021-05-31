package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.SectorType;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;

import java.util.*;

public interface TestDataTicket extends TestData {

    String TICKET_BASE_URI = BASE_URI + "/tickets";

    List<Long> TICKET_SEATS = new ArrayList<>(
        Arrays.asList(10L)
    );

    Ticket.Status TICKET_STATUS_IN_CART = Ticket.Status.IN_CART;

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

    static Set<SectorType> getSectorTypes() {
        Set<SectorType> set = new HashSet<>();
        set.add(SectorType.builder().name("Test").numberOfTickets(10).build());
        return set;
    }

    static Set<TicketType> getTicketTypes() {
        Set<TicketType> set = new HashSet<>();
        set.add(TicketType.builder()
            .title("Standard")
            .sectorType(SectorType.builder().name("Test").numberOfTickets(10).build())
            .price(1.0)
            .build());
        set.add(TicketType.builder()
            .title("VIP")
            .sectorType(SectorType.builder().name("Test").numberOfTickets(10).build())
            .price(2.33)
            .build());
        set.add(TicketType.builder()
            .title("Discount")
            .sectorType(SectorType.builder().name("Test").numberOfTickets(10).build())
            .price(0.85)
            .build());
        return set;
    }

}
