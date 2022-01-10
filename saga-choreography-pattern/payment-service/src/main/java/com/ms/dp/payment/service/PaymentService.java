package com.ms.dp.payment.service;

import com.ms.dp.common.event.PaymentEvent;
import com.ms.dp.common.event.ProductEvent;

public interface PaymentService {
	
	public PaymentEvent makePayment(ProductEvent productEvent);

	public void handlePaymentCancellation(PaymentEvent paymentEvent);

}
