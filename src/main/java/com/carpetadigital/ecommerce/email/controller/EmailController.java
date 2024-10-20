package com.carpetadigital.ecommerce.email.controller;

import com.carpetadigital.ecommerce.email.service.EmailService;
import com.carpetadigital.ecommerce.email.service.EmailType;
import com.carpetadigital.ecommerce.entity.dto.PaymentSuscriptionDto;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @GetMapping
    public String getTestion() {
       return "hola";
}

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String toEmail,
                                            @RequestParam String subject,
                                            @RequestParam String body) {
        try {
            emailService.sendProductEmail(toEmail, subject, body);
            return ResponseEntity.ok("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/send-payment")
    public ResponseEntity<String> enviarPago(@RequestBody PaymentSuscriptionDto paymentRequest) {
        try {
            log.info("Procesando pago de: ", paymentRequest.getAmount(), paymentRequest.getUserId());
            log.info(" paymentRequest" + paymentRequest );

            EmailType emailType = determineEmailType(paymentRequest);

            String formattedDateTime = paymentRequest.getFormattedDateTime();
            String amountPaid = String.valueOf(paymentRequest.getAmount());
            String operationNumber = paymentRequest.getOperationNumber();
            String voucherNumber = paymentRequest.getVoucherNumber();
            String userName = paymentRequest.getUserName();

            emailService.sendEmailBasedOnType(paymentRequest.getGuestEmail(), paymentRequest.getSubject(), formattedDateTime, amountPaid, operationNumber, voucherNumber, userName, emailType);

            return ResponseEntity.ok("Pago procesado y correo enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el pago: " + e.getMessage());
        }
    }

    private EmailType determineEmailType(PaymentSuscriptionDto paymentRequest) {
        if (paymentRequest.isSubscription()) {
            return EmailType.SUBSCRIPTION;
        } else if (paymentRequest.getUserId() != null) {
            return EmailType.LOGGED_IN_USER;
        } else if (paymentRequest.getGuestEmail() != null && !paymentRequest.getGuestEmail().isEmpty()) {
            return EmailType.GUEST_USER;
        } else {
            return EmailType.OTHER_TYPE;
        }
    }
}
