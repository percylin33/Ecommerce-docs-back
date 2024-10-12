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

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private java.sql.Timestamp paymentDate;

    @Column
    private String paymentStatus;

    @Column(name = "is_subscription", nullable = false)
    private boolean isSubscription;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentsEntity idDocument;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setIsSubscription(boolean isSubscription) {
        this.isSubscription = isSubscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
