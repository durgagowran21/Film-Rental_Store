
package com.example.filmrental.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.filmrental.model.PaymentEntity;



public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    
}

