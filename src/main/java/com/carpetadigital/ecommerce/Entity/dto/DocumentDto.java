package com.carpetadigital.ecommerce.Entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "El campo no puede estar vacío")
    @Size(min=3, max = 100, message = "El campo debe tener entre 3 y 100 caracteres")
    private String title;

    @NotBlank(message = "No puede estar vacío")
    @NotNull(message = "El campo no puede estar vacío")
    @Size(min=3, message = "El campo debe tener más de 5")
    private String description;

    @NotBlank(message = "No puede estar vacío")
    @NotNull(message = "El campo no puede estar vacío")
    @Size(min=3, max = 10, message = "El campo debe tener entre 3 y 10 caracteres")
    private String format;

    @DecimalMin("0.0")
    @NotNull(message = "El campo no puede estar vacío")
    private Float price;

    @NotBlank(message = "No puede estar vacío")
    @NotNull(message = "El campo no puede estar vacío")
    @Size(min=3, max = 50, message = "El campo debe tener entre 3 y 50 caracteres")
    private String category;

    @NotNull
    private MultipartFile file;
}
