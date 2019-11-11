package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.UserDto;
import tech.nosy.nosyemail.nosyemail.model.User;

@Mapper
public abstract class UserMapper {

    public static final UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    public abstract UserDto toUserDto(User user);
    public abstract User toUser(UserDto userDto);
}
