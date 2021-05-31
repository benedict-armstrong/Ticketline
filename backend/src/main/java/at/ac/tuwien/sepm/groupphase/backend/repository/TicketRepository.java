package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.TicketType;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/*
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByOwnerAndStatus(ApplicationUser owner, Ticket.Status status);

    List<Ticket> findByUpdateDateBeforeAndStatus(LocalDateTime pruneDate, Ticket.Status status);

    List<Ticket> findByOwnerAndTicketTypeAndStatus(ApplicationUser owner, TicketType ticketType, Ticket.Status status);

    List<Ticket> findByPerformanceAndTicketTypeSectorTypeAndStatusNot(Performance performance, SectorType sectorType, Ticket.Status status);
}
*/