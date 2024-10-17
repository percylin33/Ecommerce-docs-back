package com.carpetadigital.ecommerce.entity;



import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private java.sql.Date startDate;

    @Column(nullable = false)
    private java.sql.Date endDate;

    @Column(name = "subscription_type")
    private String subscriptionType;

    @OneToMany(mappedBy = "subscription")
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
}
