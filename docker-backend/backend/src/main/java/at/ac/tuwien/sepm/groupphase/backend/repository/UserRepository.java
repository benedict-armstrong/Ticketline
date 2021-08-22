package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    ApplicationUser findUserById(long id);

    ApplicationUser findUserByEmail(String email);

    Page<ApplicationUser> findAllByStatusNot(ApplicationUser.UserStatus status, Pageable pageable);

    List<ApplicationUser> findAllByPointsGreaterThan(Long number);

}
