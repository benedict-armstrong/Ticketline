package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCreationDateBefore(LocalDateTime pruneDate);
}
