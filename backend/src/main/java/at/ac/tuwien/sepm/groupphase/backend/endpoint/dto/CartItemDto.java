package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.NotNull;

public class CartItemDto {

    private Long id;

    @NotNull(message = "User is required")
    private UserDto userDto;

    @NotNull(message = "Event is required")
    private EventDto eventDto;

    @NotNull(message = "Sector Type is required")
    private SectorTypeDto sectorTypeDto;

    @NotNull(message = "Amount is required")
    private Integer amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(EventDto eventDto) {
        this.eventDto = eventDto;
    }

    public SectorTypeDto getSectorTypeDto() {
        return sectorTypeDto;
    }

    public void setSectorTypeDto(SectorTypeDto sectorTypeDto) {
        this.sectorTypeDto = sectorTypeDto;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
