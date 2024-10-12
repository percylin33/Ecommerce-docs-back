package com.carpetadigital.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private java.sql.Timestamp orderDate;

    @Column
    private String orderStatus;

    @ManyToMany
    @JoinTable(
            name = "order_documents",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<DocumentsEntity> documents;
}