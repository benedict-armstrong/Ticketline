package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Find all ticket entries by user and status.
     *
     * @param user of the tickets to find
     * @param status of the tickets to find
     * @return list of all found tickets
     */
    List<Ticket> findByUserAndStatus(ApplicationUser user, Ticket.Status status);

    /**
     * Find all ticket entries by status and before given time.
     *
     * @param pruneDate time that the ticket should be older then
     * @param status of the tickets to find
     * @return list of all found tickets
     */
    List<Ticket> findByChangeDateBeforeAndStatus(LocalDateTime pruneDate, Ticket.Status status);

    /**
     * Find all layoutUnit entries that are not referenced in any ticket of the given performance.
     * And are also within the given sector.
     *
     * @param performance of the tickets that seats can not be referenced
     * @param sector the seats have to be in
     * @return list of all found seats
     */
    @Query("select l from LayoutUnit l "
        + "left join Ticket t on t.seat = l and t.performance = :performance "
        + "where t.id is null and l.sector = :sector")
    List<LayoutUnit> getFreeSeatsInPerformanceAndSector(@Param("performance") Performance performance, @Param("sector") Sector sector);

    /**
    * Check if the given seat in the given performance is free.
    *
    * @param performance to check in
    * @param seat the seat
    * @return true if free, false if taken
    */
    @Query("select case when t.id is null then true else false end from LayoutUnit l "
        + "LEFT JOIN Ticket t ON t.seat = l AND t.performance = :performance "
        + "WHERE l = :seat")
    boolean checkIfSeatIsFreeByPerformance(@Param("performance") Performance performance, @Param("seat") LayoutUnit seat);

    /**
     * Find all tickets from a list of ids.
     *
     * @param ids list of the ids
     * @return list of all found tickets
     */
    @Query("select t from Ticket t where t.id in :ids")
    List<Ticket> findByIdList(@Param("ids") List<Long> ids);
}
