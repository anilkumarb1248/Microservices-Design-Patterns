package com.ms.dp.common.event;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.dto.ProductRequest;
import com.ms.dp.common.enums.ProductStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductEvent implements Event, Serializable {

	private static final long serialVersionUID = -3212911244564356687L;
	
	private UUID eventId = UUID.randomUUID();
	private LocalDate eventDate = LocalDate.now();

	private ProductRequest productRequest;
	private ProductStatus productStatus;
	private OrderRequest orderRequest;

	public ProductEvent(ProductRequest productRequest, ProductStatus productStatus, OrderRequest orderRequest) {
		this.productRequest = productRequest;
		this.productStatus = productStatus;
		this.orderRequest = orderRequest;
	}

}
