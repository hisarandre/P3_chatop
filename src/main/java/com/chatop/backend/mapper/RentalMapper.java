package com.chatop.backend.mapper;


import com.chatop.backend.dto.rental.RentalCreateRequestDto;
import com.chatop.backend.dto.rental.RentalResponseDto;
import com.chatop.backend.dto.rental.RentalUpdateRequestDto;
import com.chatop.backend.dto.rental.RentalsResponseDto;
import com.chatop.backend.entity.Rental;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    // Entity to DTO
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "owner.id", target = "owner_id")
    @Mapping(target = "picture", expression = "java(com.chatop.backend.util.UrlUtils.buildFileUrl(rental.getPicture()))")
    RentalResponseDto toRentalResponseDto(Rental rental);

    // List of Rentals to List of DTOs
    List<RentalResponseDto> toRentalResponseDtoList(List<Rental> rentals);

    // Wrap the list in the RentalsResponseDTO
    default RentalsResponseDto toRentalsResponseDto(List<Rental> rentals) {
        return new RentalsResponseDto(toRentalResponseDtoList(rentals));
    }

    // Create Rental from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true) // Set manually
    @Mapping(target = "picture", ignore = true) // Set manually
    Rental createRentalDtoToEntity(RentalCreateRequestDto dto);


    // Update Rental from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "picture", ignore = true)
    void updateRentalFromDto(RentalUpdateRequestDto dto, @MappingTarget Rental rental);
}