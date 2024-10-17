package com.carpetadigital.ecommerce.entity.dto;


import com.carpetadigital.ecommerce.entity.DocumentsEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



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

    private List<Long> documentIds; // IDs de los documentos comprados
    private String guestEmail;
    private String template;

    // Nuevos campos para manejar el envío de correos electrónicos
    private String subject;          // Asunto del correo
    private String body;            // Cuerpo del correo
    private List<String> attachments; // Archivos adjuntos (si es necesario)
    private List<String> cc;        // Correos en copia
    private List<String> bcc;       // Correos en copia oculta
}
