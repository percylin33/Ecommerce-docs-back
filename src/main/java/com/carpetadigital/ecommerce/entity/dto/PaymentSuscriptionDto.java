package com.carpetadigital.ecommerce.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuscriptionDto {





    private Long userId;
    private Double amount;
    private String paymentStatus;
    private boolean isSubscription;
    private String status;
    private String subscriptionType;








}
