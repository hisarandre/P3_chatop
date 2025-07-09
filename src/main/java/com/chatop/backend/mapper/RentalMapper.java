package com.chatop.backend.mapper;


import com.chatop.backend.dto.Rental.RentalCreateRequestDto;
import com.chatop.backend.dto.Rental.RentalResponseDto;
import com.chatop.backend.dto.Rental.RentalsResponseDto;
import com.chatop.backend.entity.Rental;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    // Entity to DTO
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(target = "picture", expression = "java(com.chatop.backend.util.UrlUtils.buildFileUrl(rental.getPicture()))")
    RentalResponseDto toRentalResponseDto(Rental rental);

    // List of Rentals to List of DTOs
    List<RentalResponseDto> toRentalResponseDtoList(List<Rental> rentals);

    // Wrap the list in the RentalsResponseDto
    default RentalsResponseDto toRentalsResponseDto(List<Rental> rentals) {
        return new RentalsResponseDto(toRentalResponseDtoList(rentals));
    }

    // Create Rental from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true) // Set manually
    @Mapping(target = "picture", ignore = true) // Set manually
    Rental createRentalDtoToEntity(RentalCreateRequestDto dto);
}