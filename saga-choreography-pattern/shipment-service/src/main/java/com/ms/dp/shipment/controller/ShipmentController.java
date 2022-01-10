package com.ms.dp.shipment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.dp.shipment.service.ShipmentService;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
	
	private ShipmentService shipmentService;

	@Autowired
	public ShipmentController(ShipmentService shipmentService) {
		super();
		this.shipmentService = shipmentService;
	}



	@PutMapping("/delevery/{orderId}")
	public void deliveryProduct(@PathVariable Integer orderId) {
		shipmentService.deliveryProduct(orderId);
	}

}
