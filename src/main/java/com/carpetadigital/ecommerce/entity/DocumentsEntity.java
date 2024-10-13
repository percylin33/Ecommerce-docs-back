package com.carpetadigital.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "file_id", nullable = false, length = 100)
    private String fileId;

    @Column(length = 50)
    private String category;

    @Column(name = "borrado_logico")
    private Boolean borradoLogico = false;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @ManyToMany(mappedBy = "documents")
    private List<Payment> payments;
}
