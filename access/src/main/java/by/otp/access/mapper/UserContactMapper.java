package by.otp.access.mapper;

import by.otp.access.dto.contact.UserContactDto;
import by.otp.access.model.UserContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserContactMapper {

    @Mapping(source = "type", target = "contactType")
    @Mapping(source = "value", target = "contactValue")
    UserContactDto toDto(UserContact contact);
}
