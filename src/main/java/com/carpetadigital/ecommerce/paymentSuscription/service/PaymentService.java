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

        if(paymentSuscriptionDto.getStatus() == null){
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
                String invitadoEmail = paymentSuscriptionDto.getGuestEmail();  // Asegúrate de que guestEmail esté en el DTO

                // Generar la boleta de compra como archivo
                File archivoBoleta = generarBoletaDePago(payment);

                // Enviar el correo con el producto y la boleta
                emailService.sendProductEmail(
                        invitadoEmail,
                        "Tu producto y boleta",
                        "Gracias por tu compra. Adjuntamos tu producto y boleta de compra.",
                        archivoBoleta
                );

                log.info("Pago por producto para invitado: " + payment);
            }
        }

        paymentRepository.save(payment);
        return true;
    }

    // Método para generar la boleta de pago como un archivo de texto (puedes cambiar a PDF si es necesario)
    public File generarBoletaDePago(Payment payment) throws IOException {
        File archivo = new File("boleta_" + payment.getUserId() + ".txt");
        FileWriter writer = new FileWriter(archivo);
        writer.write("Boleta de compra\n");
        writer.write("ID Pago: " + payment.getUserId() + "\n");
        writer.write("Fecha: " + payment.getPaymentDate() + "\n");
        writer.write("Total: " + payment.getAmount() + "\n");
        writer.write("Estado del pago: " + payment.getPaymentStatus() + "\n");
        writer.close();
        return archivo;
    }
}
