package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.TopEvent;
import at.ac.tuwien.sepm.groupphase.backend.specification.EventSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    /**
     * Find all event entries.
     *
     * @return ordered list of all events
     */
    Page<Event> findAllByOrderByStartDateAsc(Pageable pageable);

    /**
     * Find one event by id.
     *
     * @param id of the event to find
     * @return event with the id.
     */
    Event findOneById(Long id);

    /**
     * Find all events with pagination sorted by date.
     * Also contains information about ticket sales.
     *
     * @return list of all events
     */
    @Query(value = "SELECT count(t.id) as soldTickets FROM Event e "
        + "inner join Performance p on p.event_id = e.id "
        + "left join Ticket t on t.performance_id = p.id "
        + "WHERE e.start_date >= ?1 "
        + "GROUP BY e.id "
        + "ORDER BY e.start_date DESC ", nativeQuery = true)
    List<Long> findSoldTicketsOrderByStartDateDescAfterDate(LocalDate date);

    /**
     * Find all event entries that happen after the date and sorted descending.
     *
     * @return list of all events
     */
    List<Event> findAllByStartDateAfterOrderByStartDateAsc(LocalDate date);
}
