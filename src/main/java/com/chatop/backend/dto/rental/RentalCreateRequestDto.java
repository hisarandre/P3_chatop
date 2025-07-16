package com.chatop.backend.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RentalCreateRequestDto", description = "Data required to create a new rental")
public class RentalCreateRequestDto {

    @NotBlank
    @Size(max = 255)
    @Schema(
            description = "Name of the rental",
            example = "Cozy apartment",
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Surface must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    @Schema(
            description = "Surface area in square meters (must be greater than 0)",
            example = "45.5",
            minimum = "1",
            type = "number",
            format = "double",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal surface;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 10, fraction = 2)
    @Schema(
            description = "Price per month in your currency (must be 0 or positive)",
            example = "1200.00",
            minimum = "1",
            type = "number",
            format = "double",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal price;

    @NotBlank
    @Size(max = 2000)
    @Schema(
            description = "Description of the rental",
            example = "A beautiful and sunny apartment close to public transport and shops.",
            maxLength = 2000,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

    @NotNull(message = "Picture is required")
    @Schema(
            description = "Picture file to upload",
            type = "string",
            format = "binary",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private MultipartFile picture;
}
