package com.carpetadigital.ecommerce.paymentSuscription.service;

import com.carpetadigital.ecommerce.entity.Payment;
import com.carpetadigital.ecommerce.entity.Subscription;
import com.carpetadigital.ecommerce.entity.dto.PaymentSuscriptionDto;
import com.carpetadigital.ecommerce.repository.PaymentRepository;
import com.carpetadigital.ecommerce.repository.SubscriptionRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.carpetadigital.ecommerce.entity.DocumentsEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@Slf4j
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private final com.carpetadigital.ecommerce.Repository.DocumentsRepository documentsRepository;

    public Boolean processPayment(PaymentSuscriptionDto paymentSuscriptionDto) throws Exception {
        log.info("Procesando pago: " + paymentSuscriptionDto);

        if (paymentSuscriptionDto.getStatus() == null) {
            throw new Exception("El pago no se realizó correctamente");
        }

        Payment payment = new Payment();
        payment.setPaymentDate(new java.sql.Timestamp(System.currentTimeMillis()));
        payment.setUserId(paymentSuscriptionDto.getUserId());
        payment.setAmount(paymentSuscriptionDto.getAmount());
        payment.setPaymentStatus(paymentSuscriptionDto.getStatus());
        payment.setIsSubscription(paymentSuscriptionDto.isSubscription());

        if (payment.isSubscription()) {
            // Lógica de pago de suscripción
            Subscription subscription = new Subscription();
            subscription.setUserId(paymentSuscriptionDto.getUserId());
            subscription.setStatus(paymentSuscriptionDto.getStatus());
            subscription.setSubscriptionType(paymentSuscriptionDto.getSubscriptionType());

            Date sqlStartDate = new Date(System.currentTimeMillis());
            subscription.setStartDate(sqlStartDate);

            LocalDate startLocalDate = sqlStartDate.toLocalDate();
            LocalDate endLocalDate = startLocalDate.plusMonths(12);
            Date sqlEndDate = Date.valueOf(endLocalDate);

            subscription.setEndDate(sqlEndDate);
            Subscription su = subscriptionRepository.save(subscription);
            payment.setSubscription(su);

            log.info("Suscripción exitosa: " + subscription);
        } else {
            // Lógica de orden de compra
            List<DocumentsEntity> documents = documentsRepository.findAllById(paymentSuscriptionDto.getDocumentIds());
            payment.setDocuments(documents);

            if (paymentSuscriptionDto.getUserId() != null) {
                log.info("Compra de producto " + payment);
            } else {
                payment.setUserId(null);  // Si el campo permite nulos, deja el ID del usuario como nulo

                // Extraer el correo del invitado desde el DTO
                String invitadoEmail = paymentSuscriptionDto.getGuestEmail();

                // Verificar que el correo del invitado no sea nulo
                if (invitadoEmail == null || invitadoEmail.isEmpty()) {
                    throw new IllegalArgumentException("To address must not be null");
                }

                payment.setUserId(Long.valueOf(1));
                Payment payment1 = paymentRepository.save(payment);
                // Generar la boleta de compra como archivo


                String htmlBuilder = generarBoletaDePago(payment1);

                // Enviar el correo con el producto y la boleta
                String subject = paymentSuscriptionDto.getSubject();
                emailService.sendProductEmail(invitadoEmail, subject, htmlBuilder);
            }
        }

        paymentRepository.save(payment);
        return true;
    }

    // Método para generar la boleta de pago como un archivo de texto (puedes cambiar a PDF si es necesario)
    public String generarBoletaDePago(Payment payment) throws IOException {
        String formattedDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payment.getPaymentDate());
        String amountPaid = String.valueOf(payment.getAmount());
        Long operationNumber = payment.getPaymentId(); // Reemplaza con el número de operación real

        return  "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title></title></head><body style='color:black'>" +
                "<div style='width: 100%'>" +
                "<div style='display:flex;'>" +
                "</div>" +
                "<h1 style='margin-top: 2px ;text-align: center;font-weight: bold;font-style: italic;'>Voucher de Pago</h1>" +
                "<h2 style='text-align: center;'>Fecha de Pago :" + formattedDateTime + "  </h2>" +
                "<h2 style='text-align: center;'>Monto Pagado: $" + amountPaid + "  </h2>" +
                "<h2 style='text-align: center;'>Cod. de transacción :" + operationNumber + "  </h2>" +
                "<h2 style='text-align: center;'>¡Gracias por su pago!</h2>" +
                "<center><p style='margin-left: 10%;margin-right: 10%;'>Te saluda la familia Carpeta Digital</p></center> " +
                "<center><div style='width: 100%'>" +
                "</a></div></center>" +
                "<center><div style='width: 100%'>" +
                "<p style='margin-left: 10%;margin-right: 10%; '></p>" +
                "<center>Recuerde que el pago también lo puede realizar mediante depósito en nuestra cuenta corriente a través de Agente BCP, Agencia BCP o transferencia bancaria desde Banca por Internet.</center>" +
                "</div></center>" +
                "<center><div style='width: 100%'>" +
                "<p style='margin-left: 10%;margin-right: 10%; '>----------------------------</p>" +
                "</div></center>" +
                "</div></center>" +
                "</body>" +
                "</html>";
    }

}
