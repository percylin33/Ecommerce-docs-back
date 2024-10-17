package com.carpetadigital.ecommerce.Repository;

import com.carpetadigital.ecommerce.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface StateRepository extends JpaRepository<State, Long> {
}
