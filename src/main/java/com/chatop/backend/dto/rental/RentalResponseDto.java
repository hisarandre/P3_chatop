package com.chatop.backend.dto.rental;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDto {

    private Integer id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;
    private Integer owner_id;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime created_at;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updated_at;
}