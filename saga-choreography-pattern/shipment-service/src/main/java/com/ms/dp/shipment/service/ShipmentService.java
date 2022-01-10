package com.ms.dp.shipment.service;

import com.ms.dp.common.event.NotifyEvent;
import com.ms.dp.common.event.PaymentEvent;

public interface ShipmentService {
	
	public NotifyEvent shipProduct(PaymentEvent paymentEvent);

	public void deliveryProduct(Integer orderId);
	
	

}
