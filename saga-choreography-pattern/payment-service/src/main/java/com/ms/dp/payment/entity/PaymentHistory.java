package com.ms.dp.payment.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ms.dp.common.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PAYMENT_HISTORY")
@Builder
public class PaymentHistory {

	@Id
	private Integer orderId;
	private String userId;
	private Integer productId;
	private Integer quantity;
	private Integer amount;
	private LocalDate paymentDate;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

}
