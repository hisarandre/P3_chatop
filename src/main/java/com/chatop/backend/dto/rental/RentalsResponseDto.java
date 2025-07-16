package com.chatop.backend.dto.rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalsResponseDto {

    private List<RentalResponseDto> rentals;
}