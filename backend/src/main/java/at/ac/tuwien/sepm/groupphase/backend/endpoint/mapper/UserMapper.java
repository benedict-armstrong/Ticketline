package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper(uses = {NewsMapper.class, AddressMapper.class})
public interface UserMapper extends FileTypeMapper {

    ApplicationUser userDtoToApplicationUser(UserDto userDto);

    UserDto applicationUserToUserDto(ApplicationUser user);

}
