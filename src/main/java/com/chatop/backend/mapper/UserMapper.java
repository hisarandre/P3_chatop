package com.chatop.backend.mapper;


import com.chatop.backend.dto.Rental.RentalResponseDto;
import com.chatop.backend.dto.Rental.RentalsResponseDto;
import com.chatop.backend.dto.UserResponseDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity to DTO
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    UserResponseDto toUserResponseDto(User user);

}