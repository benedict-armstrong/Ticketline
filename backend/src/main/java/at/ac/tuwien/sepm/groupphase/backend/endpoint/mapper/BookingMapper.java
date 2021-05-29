package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {TicketMapper.class, UserMapper.class, FileMapper.class})
public interface BookingMapper {

    BookingDto bookingToBookingDto(Booking booking);

    Booking bookingDtoToBooking(BookingDto bookingDto);

    List<BookingDto> bookingListToBookingDtoList(List<Booking> booking);
}
