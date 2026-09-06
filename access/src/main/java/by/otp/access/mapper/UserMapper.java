package by.otp.access.mapper;

import by.otp.access.dto.user.UserDto;
import by.otp.access.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserContactMapper.class)
public interface UserMapper {

    @Mapping(source = "contactList", target = "contacts")
    @Mapping(source = "roleList", target = "roles")
    UserDto userToUserDto(User user);
}
