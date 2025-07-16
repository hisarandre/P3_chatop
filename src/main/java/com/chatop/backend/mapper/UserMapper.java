package com.chatop.backend.mapper;


import com.chatop.backend.dto.user.UserResponseDto;
import com.chatop.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity to DTO
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    UserResponseDto toUserResponseDto(User user);

}