package com.carpetadigital.ecommerce.entity.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuscriptionDto {

    private Long userId;
    private Double amount;
    @JsonProperty("isSubscription")
    private boolean isSubscription;
    private String status;
    private String subscriptionType;
    private String idDocument;


}
