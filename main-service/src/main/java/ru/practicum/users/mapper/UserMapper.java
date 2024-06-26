package ru.practicum.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import javax.annotation.processing.Generated;
import java.util.List;

@Mapper(componentModel = "spring")
@Generated("AutoGeneratedByMapStruct")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User newUserToUser(NewUserRequest newUserRequest);

    UserDto userToUserDto(User user);

    List<UserDto> userToUserDto(List<User> users);
}