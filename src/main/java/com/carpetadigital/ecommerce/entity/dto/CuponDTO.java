package com.carpetadigital.ecommerce.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuponDTO {
        private String code;
        private Double discountValue;
        private LocalDate startDate;
        private LocalDate expirationDate;
        private Boolean isActive;

}
