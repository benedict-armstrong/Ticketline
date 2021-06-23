package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSearch;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.specification.PerformanceSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomPerformanceService implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PerformanceRepository performanceRepository;
    private final TicketService ticketService;

    @Autowired
    public CustomPerformanceService(PerformanceRepository performanceRepository,  TicketService ticketService) {
        this.performanceRepository = performanceRepository;
        this.ticketService = ticketService;
    }

    @Override
    public Performance findById(long id) {
        LOGGER.trace("findById({})", id);

        Performance performance = performanceRepository.findOneById(id);

        if (performance == null) {
            throw new NotFoundException("The performance with this id was not found.");
        }

        Venue venue = performance.getVenue();
        List<LayoutUnit> newSeats = venue.getLayout();
        LOGGER.info("New Seats : {}", newSeats.size());

        List<LayoutUnit> takenSeats = ticketService.getTakenSeatsInPerformance(performance);
        LOGGER.info("Taken Seats : {}", takenSeats.size());

        for (LayoutUnit seat : newSeats) {
            seat.setFree(true);
            for (LayoutUnit takenSeat : takenSeats) {
                if (takenSeat.getId().equals(seat.getId())) {
                    LOGGER.info("Current Seat: {}", seat);
                    seat.setFree(false);
                    break;
                }
            }
        }

        LOGGER.info("New Seats : {}", newSeats);


        venue.setLayout(newSeats);
        performance.setVenue(venue);


        LOGGER.info("Performance Seats : {}", performance.getVenue().getLayout());

        return performance;
    }

    @Override
    public List<Performance> findAll(Pageable pageable) {
        LOGGER.trace("findAllPerformances()");
        return performanceRepository.findAll(pageable).getContent();
    }

    @Override
    public Performance addPerformance(Performance performance) {
        LOGGER.trace("addEvent({})", performance);
        return performanceRepository.save(performance);
    }

    @Override
    public List<Performance> search(PerformanceSearch performance, Pageable pageable) {
        LOGGER.trace("searchPerformance({})", performance);

        PerformanceSpecificationBuilder builder = new PerformanceSpecificationBuilder();

        if (performance.getDate() != null) {
            builder.with("date", "+", performance.getDate());
        }

        if (performance.getEventId() != null) {
            builder.with("event", ":", performance.getEventId());
        }

        if (performance.getVenue() != null) {
            builder.with("venue", ":", performance.getVenue());
        }

        return performanceRepository.findAll(builder.build(), pageable).getContent();
    }

    @Override
    @Scheduled(cron = "0 * * * * ?")
    public void prunePerformance() {
        LOGGER.trace("prunePerformance()");
        LocalDateTime pruneDateFuture = LocalDateTime.now().plusMinutes(30);
        LocalDateTime pruneDatePast = LocalDateTime.now().minusMinutes(5);
        List<Performance> performances = performanceRepository.findAllByDateBetween(pruneDatePast, pruneDateFuture);
        ticketService.pruneReservations(performances);
    }

    @Override
    public List<Performance> findAllPerformancesByVenueAddress(Long addressId, Pageable pageable) {
        return performanceRepository.findAllByVenue_Address_Id(addressId, pageable).getContent();
    }

    @Override
    public List<Performance> findAllPerformancesByArtist(Long artistId, Pageable pageable) {
        return performanceRepository.findAllByArtistId(artistId, pageable).getContent();
    }
}