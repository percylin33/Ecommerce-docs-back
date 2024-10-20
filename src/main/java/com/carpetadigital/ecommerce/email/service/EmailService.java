package com.carpetadigital.ecommerce.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private String downloadUrl;
    private String formattedDateTime;

    // Método para enviar correo basado en el tipo
    public void sendEmailBasedOnType(String toEmail, String subject, String formattedDateTime, String amountPaid, String operationNumber, String voucherNumber, String userName, EmailType emailType) throws MessagingException {
        String htmlContent;
        log.info("emailType: " + emailType);

        switch (emailType) {
            case SUBSCRIPTION:
                String subscriptionType = "Anual"; // o "Mensual", según sea necesario
                htmlContent = generateSubscriptionTemplate(formattedDateTime, amountPaid, operationNumber, userName, subscriptionType);
                log.info("emailTypeSubscription: " + emailType);
                break;

            case LOGGED_IN_USER:
                htmlContent = generateLoggedInUserTemplate(formattedDateTime, amountPaid, operationNumber, userName, downloadUrl);
                break;

            case GUEST_USER:
                htmlContent = generateGuestUserTemplate(formattedDateTime, amountPaid, operationNumber, voucherNumber, toEmail);
                break;
            case OTHER_TYPE:
                htmlContent = generateWelcomeEmailTemplate(formattedDateTime);
                log.info("emailTypeOther: " + emailType);
                break;
            default:
                throw new IllegalArgumentException("Unknown email type");
        }

        sendProductEmail(toEmail, subject, htmlContent);
    }

    // Método para enviar un correo
    public void sendProductEmail(String toEmail, String subject, String body) throws MessagingException {
        log.info("Iniciando el envío de correo a: " + toEmail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            // Enviar el correo
            mailSender.send(message);
            log.info("Envío exitoso a: " + toEmail);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo a: " + toEmail, e);
            throw e;  // Para propagar la excepción
        }
    }

    // Método para generar plantilla de suscripción
    private String generateSubscriptionTemplate(String formattedDateTime, String amountPaid, String operationNumber, String userName, String subscriptionType) {
        return "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Voucher de Suscripción</title></head>" +
                "<body style='color:black'>" +
                "<div style='width: 100%'>" +
                "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Voucher de Suscripción</h1>" +
                "<h2 style='text-align: center;'>¡Gracias por tu compra, " + userName + "!</h2>" +
                "<h2 style='text-align: center;'>Tipo de Suscripción: " + subscriptionType + "</h2>" +
                "<h2 style='text-align: center;'>Fecha de Pago: " + formattedDateTime + "</h2>" +
                "<h2 style='text-align: center;'>Monto Pagado: $" + amountPaid + "</h2>" +
                "<h2 style='text-align: center;'>Cod. de transacción: " + operationNumber + "</h2>" +
                "<h2 style='text-align: center;'>¡Gracias por unirte a nosotros!</h2>" +
                "<center><p style='margin-left: 10%;margin-right: 10%;'>Te saluda la familia Carpeta Digital</p></center>" +
                "<center>Recuerde que el pago también lo puede realizar mediante depósito en nuestra cuenta corriente a través de Agente BCP, Agencia BCP o transferencia bancaria desde Banca por Internet.</center>" +
                "<center><div style='width: 100%'><p style='margin-left: 10%;margin-right: 10%;'>----------------------------</p></div></center>" +
                "</div></body></html>";
    }

    // Método para generar plantilla de usuario logueado
    private String generateLoggedInUserTemplate(String formattedDateTime, String amountPaid, String operationNumber, String userName, String downloadUrl) {
        return "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Voucher de Pago</title></head>" +
                "<body style='color:black'>" +
                "<div style='width: 100%'>" +
                "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Voucher de Pago</h1>" +
                "<h2 style='text-align: center;'>¡Gracias por tu compra, " + userName + "!</h2>" +
                "<h2 style='text-align: center;'>Fecha de Pago: " + formattedDateTime + "</h2>" +
                "<h2 style='text-align: center;'>Monto Pagado: $" + amountPaid + "</h2>" +
                "<h2 style='text-align: center;'>Cod. de transacción: " + operationNumber + "</h2>" +
                "<h2 style='text-align: center;'>¡Gracias por su pago!</h2>" +
                "<center><p style='margin-left: 10%;margin-right: 10%;'>Te saluda la familia Carpeta Digital</p></center>" +
                "<center><p>Aquí está la <a href='" + downloadUrl + "'>URL para descargar tu producto</a>.</p></center>" +
                "<center><div style='width: 100%'></div></center>" +
                "<center>Recuerde que el pago también lo puede realizar mediante depósito en nuestra cuenta corriente a través de Agente BCP, Agencia BCP o transferencia bancaria desde Banca por Internet.</center>" +
                "<center><div style='width: 100%'><p style='margin-left: 10%;margin-right: 10%;'>----------------------------</p></div></center>" +
                "</div></body></html>";
    }

    // Método para generar plantilla de usuario invitado
    private String generateGuestUserTemplate(String formattedDateTime, String amountPaid, String operationNumber, String voucherNumber, String email) {
        return "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Voucher de Pago</title></head>" +
                "<body style='color:black'>" +
                "<div style='width: 100%'>" +
                "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Voucher de Pago</h1>" +
                "<h2 style='text-align: center;'>Correo del Usuario: " + email + "</h2>" +
                "<h2 style='text-align: center;'>Fecha de Pago: " + formattedDateTime + "</h2>" +
                "<h2 style='text-align: center;'>Monto Pagado: $" + amountPaid + "</h2>" +
                "<h2 style='text-align: center;'>Cod. de transacción: " + operationNumber + "</h2>" +
                "<h2 style='text-align: center;'>¡Gracias por su pago!</h2>" +
                "<center><p style='margin-left: 10%;margin-right: 10%;'>Te saluda la familia Carpeta Digital</p></center>" +
                "<center><div style='width: 100%'></div></center>" +
                "<center>Recuerde que el pago también lo puede realizar mediante depósito en nuestra cuenta corriente a través de Agente BCP, Agencia BCP o transferencia bancaria desde Banca por Internet.</center>" +
                "<center><div style='width: 100%'><p style='margin-left: 10%;margin-right: 10%;'>----------------------------</p></div></center>" +
                "</div></body></html>";
    }

    // Método para generar plantilla de bienvenida
    private String generateWelcomeEmailTemplate(String userName) {
        return "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Bienvenido a Carpeta Digital</title>" +
                "<style>" +
                "body { color: black; font-family: Arial, sans-serif; }" +
                "h1 { text-align: center; font-weight: bold; }" +
                "h2 { text-align: center; }" +
                "p { text-align: center; }" +
                "div { width: 100%; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "<h1>¡Bienvenido a Carpeta Digital!</h1>" +
                "<h2>¡Gracias por unirte a nosotros, " + userName + "!</h2>" +
                "<h3>Fecha de registro: " + formattedDateTime + "</h3>" +
                "<p>Estamos emocionados de tenerte a bordo y esperamos que disfrutes de todos los servicios que ofrecemos.</p>" +
                "<p>Te saluda la familia Carpeta Digital</p>" +
                "<div style='text-align: center;'>" +
                "<p>----------------------------</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
