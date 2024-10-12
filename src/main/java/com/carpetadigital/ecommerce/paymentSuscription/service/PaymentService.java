package com.carpetadigital.ecommerce.paymentSuscription.service;

import com.carpetadigital.ecommerce.User.User;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

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

    public void processPayment(PaymentSuscriptionDto paymentSuscriptionDto) {


        Payment payment = new Payment();
        payment.setPaymentDate(new java.sql.Timestamp(System.currentTimeMillis()));
        payment.setUserId(paymentSuscriptionDto.getUserId());
        payment.setAmount(paymentSuscriptionDto.getAmount());
        payment.setPaymentStatus(paymentSuscriptionDto.getStatus());
        payment.setSubscription(true);

        log.info("Fecha de pago: " + payment.getPaymentDate());


        // Diferenciar entre suscripción y pago por producto
        if (payment.isSubscription()) {
            // Pago de suscripción
            Subscription subscription = new Subscription();

            // Configuramos los detalles de la suscripción
            subscription.setUserId(paymentSuscriptionDto.getUserId());
            subscription.setStatus(paymentSuscriptionDto.getStatus());
            subscription.setSubscriptionType(paymentSuscriptionDto.getSubscriptionType());

            // Establecemos la fecha de inicio de la suscripción (java.sql.Date con la fecha actual)
            Date sqlStartDate = new Date(System.currentTimeMillis());
            subscription.setStartDate(sqlStartDate);


            LocalDate startLocalDate = sqlStartDate.toLocalDate();
            LocalDate endLocalDate = startLocalDate.plusMonths(12);
            Date sqlEndDate = Date.valueOf(endLocalDate);

            subscription.setEndDate(sqlEndDate);
            Subscription su = subscriptionRepository.save(subscription);



            System.out.println("Fecha de finalización: " + sqlEndDate);
            log.info("Suscripción exitosa: " + subscription);

            // Guardar la suscripción en la base de datos
            // paymentSuscriptionDto.save(subscription);
            log.info("Pago procesado: " + payment);

        } else {
            // Lógica para el pago por producto
            log.info("compra de producto " + payment);
        }

        // Guardar el pago en la base de datos
        paymentRepository.save(payment);
    }

}
