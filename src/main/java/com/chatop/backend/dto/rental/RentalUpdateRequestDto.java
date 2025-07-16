package com.chatop.backend.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "RentalUpdateRequestDto",
        description = "DTO used to update an existing rental"
)
public class RentalUpdateRequestDto {

    @Schema(
            description = "Updated rental name",
            example = "Cozy apartment",
            maxLength = 255
    )
    @NotBlank
    @Size(max = 255)
    private String name;

    @Schema(
            description = "Updated surface in square meters. Must be greater than 0.",
            example = "45.5",
            minimum = "0.01"
    )
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Surface must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal surface;

    @Schema(
            description = "Updated price per month. Must be 0 or greater.",
            example = "1200",
            minimum = "0"
    )
    @NotNull
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @Schema(
            description = "Updated rental description",
            example = "A beautiful and sunny apartment close to public transport and shops.",
            maxLength = 2000
    )
    @NotBlank
    @Size(max = 2000)
    private String description;
}