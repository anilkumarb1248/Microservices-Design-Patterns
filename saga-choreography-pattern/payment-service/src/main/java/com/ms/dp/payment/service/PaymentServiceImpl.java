package com.ms.dp.payment.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.dto.PaymentRequest;
import com.ms.dp.common.dto.ProductRequest;
import com.ms.dp.common.enums.PaymentStatus;
import com.ms.dp.common.event.PaymentEvent;
import com.ms.dp.common.event.ProductEvent;
import com.ms.dp.payment.entity.PaymentHistory;
import com.ms.dp.payment.entity.User;
import com.ms.dp.payment.repository.PaymentRepository;
import com.ms.dp.payment.repository.UserRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	private UserRepository userRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public PaymentServiceImpl(UserRepository userRepository, PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.paymentRepository = paymentRepository;
	}

	@Transactional
	@Override
	public PaymentEvent makePayment(ProductEvent productEvent) {
		
		ProductRequest productRequest = productEvent.getProductRequest();
		OrderRequest orderRequest = productEvent.getOrderRequest();
		
		Optional<User> optional = userRepository.findById(orderRequest.getUserId());

		if (!optional.isPresent()) {
			return createPaymentEvent(PaymentStatus.PAYMENT_FAILED, orderRequest, productRequest);
		}
		User user = optional.get();
		Integer totalAmount = productRequest.getAmount();
		if (user.getAvailableAmount() < totalAmount) {
			return createPaymentEvent(PaymentStatus.INSUFFICIENT_FUNDS, orderRequest, productRequest);
		}

		// Deducting the Amount
		user.setAvailableAmount(user.getAvailableAmount() - totalAmount);
		userRepository.save(user);
		
		// Saving the payment details
		PaymentHistory paymentHistory = PaymentHistory.builder()
				.orderId(orderRequest.getOrderId())
				.userId(orderRequest.getUserId())
				.productId(productEvent.getProductRequest().getProductId())
				.quantity(productEvent.getProductRequest().getQuantity())
				.amount(productEvent.getProductRequest().getAmount())
				.paymentDate(LocalDate.now())
				.paymentStatus(PaymentStatus.PAYMENT_COMPLETED)
				.build();
		paymentRepository.save(paymentHistory);

		return createPaymentEvent(PaymentStatus.PAYMENT_COMPLETED, orderRequest,
				productRequest);
	}
	
	private PaymentEvent createPaymentEvent(PaymentStatus status, OrderRequest orderRequest, ProductRequest productRequest) {
		
		PaymentRequest paymentRequest = PaymentRequest.builder()
		.orderId(orderRequest.getOrderId())
		.userId(orderRequest.getUserId())
		.amount(productRequest.getAmount())
		.build();
		orderRequest.setPaymentStatus(status);
		return new PaymentEvent(paymentRequest, status, orderRequest, productRequest);
		
	}

	@Override
	@Transactional
	public void handlePaymentCancellation(PaymentEvent paymentEvent) {
		PaymentRequest paymentRequest= paymentEvent.getPaymentRequest();
		
//		Optional<User> userOptional = userRepository.findById(paymentRequest.getUserId());
//		if(userOptional.isPresent()) {
//			User user = userOptional.get();
//			user.setAvailabileAmount(user.getAvailabileAmount() + paymentRequest.getAmount());
//			userRepository.save(user);
//		}
		
		userRepository.findById(paymentRequest.getUserId())
        	.ifPresent(user->user.setAvailableAmount(user.getAvailableAmount() + paymentRequest.getAmount()));
		
		paymentRepository.findById(paymentRequest.getOrderId())
			.ifPresent(paymentHistory-> paymentHistory.setPaymentStatus(PaymentStatus.PAYMENT_ROLLBACKED));
		
	}

}
