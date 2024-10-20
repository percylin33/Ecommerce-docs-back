package com.carpetadigital.ecommerce.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuscriptionDto {

    private Long userId; // ID del usuario
    private Double amount; // Monto pagado

    @JsonProperty("isSubscription")
    private boolean isSubscription; // Indica si es una suscripción

    private String status; // Estado del pago
    private String subscriptionType; // Tipo de suscripción

    private List<Long> documentIds; // IDs de los documentos comprados
    private String guestEmail; // Correo del invitado

    // Nuevos campos para manejar el envío de correos electrónicos
    private String subject; // Asunto del correo
    private String body; // Cuerpo del correo
    private String formattedDateTime; // Fecha y hora formateada del pago
    private String operationNumber; // Número de operación del pago
    private String voucherNumber; // Número de voucher
    private String userName; // Nombre del usuario

    // Campos para correos en copia
    private List<String> cc; // Correos en copia
    private List<String> bcc; // Correos en copia oculta
}
