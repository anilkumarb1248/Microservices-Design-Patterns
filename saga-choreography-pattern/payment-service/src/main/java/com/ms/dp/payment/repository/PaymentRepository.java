package com.ms.dp.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.dp.payment.entity.PaymentHistory;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentHistory, Integer> {

}
