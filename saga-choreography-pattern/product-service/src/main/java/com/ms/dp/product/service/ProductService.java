package com.ms.dp.product.service;import com.ms.dp.common.event.OrderEvent;
import com.ms.dp.common.event.ProductEvent;

public interface ProductService {
	
	public ProductEvent checkProductsAvailability(OrderEvent orderEvent);

	public void handleProductCancellation(ProductEvent productEvent);

}
