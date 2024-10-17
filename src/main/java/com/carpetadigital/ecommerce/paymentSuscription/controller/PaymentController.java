package com.carpetadigital.ecommerce.paymentSuscription.controller;




import com.carpetadigital.ecommerce.entity.Payment;
import com.carpetadigital.ecommerce.entity.dto.PaymentSuscriptionDto;
import com.carpetadigital.ecommerce.paymentSuscription.service.PaymentService;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/send")
    public ResponseEntity<String> enviarPago(@RequestBody PaymentSuscriptionDto paymentRequest) {
        try {
            log.info("Procesando pago de: {} para el usuario: {}", paymentRequest.getAmount(), paymentRequest.getUserId());

            // Aquí se puede procesar el envío del correo usando los campos de paymentRequest
            log.info("Enviando correo a: {}", paymentRequest.getGuestEmail());
            log.info("Asunto: {}", paymentRequest.getSubject());
            log.info("Cuerpo: {}", paymentRequest.getBody());

            // Adjuntar archivos (si es necesario)
            if (paymentRequest.getAttachments() != null && !paymentRequest.getAttachments().isEmpty()) {
                log.info("Adjuntando archivos: {}", paymentRequest.getAttachments());
            }

            return ResponseEntity.ok("Pago procesado y correo enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el pago: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody @Valid PaymentSuscriptionDto paymentSuscriptionDto) throws Exception {
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                paymentService.processPayment(paymentSuscriptionDto),
                true
        );
    }



    }



