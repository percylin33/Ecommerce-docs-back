package com.carpetadigital.ecommerce.repository;




import com.carpetadigital.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
