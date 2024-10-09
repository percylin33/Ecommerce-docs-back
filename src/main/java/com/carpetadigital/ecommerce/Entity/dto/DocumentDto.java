package com.carpetadigital.ecommerce.Entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    @NotBlank(message = "No puede estar vacío")
    private String title;

    @NotBlank(message = "No puede estar vacío")
    private String description;

    @NotBlank(message = "No puede estar vacío")
    private String format;

    @DecimalMin("0.0")
    private BigDecimal price;

    @NotBlank(message = "No puede estar vacío")
    private String category;

    @NotNull
    private MultipartFile file;
}
