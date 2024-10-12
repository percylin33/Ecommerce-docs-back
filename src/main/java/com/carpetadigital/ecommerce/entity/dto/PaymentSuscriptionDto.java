package com.carpetadigital.ecommerce.entity.dto;


import com.carpetadigital.ecommerce.entity.DocumentsEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<Long> documentIds;


}
