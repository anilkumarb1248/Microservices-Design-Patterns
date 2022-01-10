package com.ms.dp.shipment.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.dp.common.dto.NotifyRequest;
import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.dto.ProductRequest;
import com.ms.dp.common.enums.DeliveryStatus;
import com.ms.dp.common.event.NotifyEvent;
import com.ms.dp.common.event.PaymentEvent;
import com.ms.dp.shipment.entity.ShipmentDetails;
import com.ms.dp.shipment.entity.UserAddress;
import com.ms.dp.shipment.repository.ShipmentRepository;
import com.ms.dp.shipment.repository.UserAddressRepository;

@Service
public class ShipmentServiceImpl implements ShipmentService {

	private UserAddressRepository userAddressRepository;
	private ShipmentRepository shipmentRepository;
	private KafkaEventProducerService kafkaEventProducerService;

	@Autowired
	public ShipmentServiceImpl(UserAddressRepository userAddressRepository, ShipmentRepository shipmentRepository,
			KafkaEventProducerService kafkaEventProducerService) {
		this.userAddressRepository = userAddressRepository;
		this.shipmentRepository = shipmentRepository;
		this.kafkaEventProducerService = kafkaEventProducerService;
	}

	@Override
	@Transactional
	public NotifyEvent shipProduct(PaymentEvent paymentEvent) {
		OrderRequest orderRequest = paymentEvent.getOrderRequest();
		ProductRequest productRequest = paymentEvent.getProductRequest();

		ShipmentDetails shipmentDetails = ShipmentDetails.builder()
				.orderId(orderRequest.getOrderId())
				.userId(orderRequest.getUserId())
				.productId(orderRequest.getProductId())
				.productName(productRequest.getProductName())
				.quantity(orderRequest.getQuantity())
				.amount(productRequest.getAmount())
				.build();

		Optional<UserAddress> optional = userAddressRepository.findById(orderRequest.getAddressId());

		if (!optional.isPresent()) {
			return createNotifyEvent(shipmentDetails, DeliveryStatus.DELIVERY_FAILIED, paymentEvent);
		}

		shipmentDetails.setAddress(optional.get().getAddress());
		shipmentDetails.setDeliveryStatus(DeliveryStatus.DELIVERY_INITIATED);
		shipmentRepository.save(shipmentDetails);

		return createNotifyEvent(shipmentDetails, DeliveryStatus.DELIVERY_INITIATED, paymentEvent);
	}

	private NotifyEvent createNotifyEvent(ShipmentDetails shipmentDetails, DeliveryStatus status, PaymentEvent paymentEvent) {
		NotifyRequest notifyRequest = new NotifyRequest();
		BeanUtils.copyProperties(shipmentDetails, notifyRequest);
		paymentEvent.getOrderRequest().setDeliveryStatus(status);
		return new NotifyEvent(notifyRequest, status, paymentEvent.getOrderRequest(),paymentEvent.getProductRequest(), paymentEvent.getPaymentRequest());
	}

	@Override
	@Transactional
	public void deliveryProduct(Integer orderId) {
		Optional<ShipmentDetails> optional =  shipmentRepository.findById(orderId);
		if(optional.isPresent()) {
			ShipmentDetails shipmentDetails = optional.get();
			shipmentDetails.setDeliveryStatus(DeliveryStatus.DELIVERY_COMPLETED);
			shipmentRepository.save(shipmentDetails);
			
			NotifyRequest notifyRequest = new NotifyRequest();
			BeanUtils.copyProperties(shipmentDetails, notifyRequest);
			
			OrderRequest orderRequest = new OrderRequest();
			orderRequest.setOrderId(shipmentDetails.getOrderId());
			orderRequest.setUserId(shipmentDetails.getUserId());
			orderRequest.setProductId(shipmentDetails.getProductId());
			orderRequest.setQuantity(shipmentDetails.getQuantity());
			orderRequest.setAmount(shipmentDetails.getAmount());
			orderRequest.setDeliveryStatus(DeliveryStatus.DELIVERY_COMPLETED);
			
			kafkaEventProducerService.publishEvents(new NotifyEvent(notifyRequest, DeliveryStatus.DELIVERY_COMPLETED, orderRequest));
	
		}
		
	}

}
