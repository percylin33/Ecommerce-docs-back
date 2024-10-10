package com.carpetadigital.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "documents")
public class DocumentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column( columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 10)
    private String format;

    @Column(nullable = false)
    private Float price;

    @Column(name = "file_url", nullable = false, length = 254)
    private String fileUrl;

    @Column(length = 50)
    private String category;

    @Column(name = "borrado_logico")
    private Boolean borradoLogico = false;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();
}
