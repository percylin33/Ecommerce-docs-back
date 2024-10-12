package com.carpetadigital.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private Long userId;

    // Relación con la entidad Subscription (si aplica)
    @ManyToOne
    @JoinColumn(name = "subscription_id")  // Nombre de la columna de la relación en la base de datos
    private Subscription subscription;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private java.sql.Timestamp paymentDate;

    @Column
    private String paymentStatus;

    // Campo booleano que indica si es una suscripción o no
    @Column(name = "is_subscription", nullable = false)
    private boolean isSubscription;

    // Getters y Setters
    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean subscription) {
        this.isSubscription = subscription;
    }

}
