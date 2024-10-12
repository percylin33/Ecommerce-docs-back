package com.carpetadigital.ecommerce.repository;




import com.carpetadigital.ecommerce.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
