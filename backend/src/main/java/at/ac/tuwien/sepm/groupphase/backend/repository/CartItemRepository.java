package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserAndStatus(ApplicationUser owner, CartItem.Status status);

    List<CartItem> findByChangeDateBeforeAndStatus(LocalDateTime pruneDate, CartItem.Status status);

    //List<Order> findByUserAndTicketTypeAndStatus(ApplicationUser owner, TicketType ticketType, Order.Status status);

    //List<Order> findByPerformanceAndTicketTypeSectorTypeAndStatusNot(Performance performance, SectorType sectorType, Order.Status status);
}
