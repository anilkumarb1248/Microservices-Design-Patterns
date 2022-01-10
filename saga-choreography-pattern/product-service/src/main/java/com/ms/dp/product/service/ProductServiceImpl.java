package com.ms.dp.product.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.dto.ProductRequest;
import com.ms.dp.common.enums.ProductStatus;
import com.ms.dp.common.event.OrderEvent;
import com.ms.dp.common.event.ProductEvent;
import com.ms.dp.product.entity.Product;
import com.ms.dp.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	@Override
	public ProductEvent checkProductsAvailability(OrderEvent orderEvent) {
		OrderRequest orderRequest = orderEvent.getOrderRequest();
		Optional<Product> optional = productRepository.findById(orderRequest.getProductId());

		if (!optional.isPresent()) {
			return createProductEvent(orderRequest, "", 0, ProductStatus.PRODUCT_NOT_AVAILABLE);
		}

		Product product = optional.get();
		if (product.getAvailabilityCount() == 0 || product.getAvailabilityCount() < orderRequest.getQuantity()) {
			ProductStatus productStatus = product.getAvailabilityCount() == 0 ? ProductStatus.PRODUCTS_OUT_OF_STOCK
					: ProductStatus.PRODUCTS_INSUFFICIENT;
			return createProductEvent(orderRequest, product.getProductName(), 0, productStatus);
		}

		product.setAvailabilityCount(product.getAvailabilityCount() - orderRequest.getQuantity());
		productRepository.save(product);

		Integer totalAmount = orderRequest.getQuantity() * product.getPrice();
		
		return createProductEvent(orderRequest, product.getProductName(), totalAmount,
				ProductStatus.PRODUCTS_AVAILABLE);
	}

	private ProductEvent createProductEvent(OrderRequest orderRequest, String productName, Integer totalAmount,
			ProductStatus productStatus) {
		
		ProductRequest productRequest = ProductRequest.builder()
				.productId(orderRequest.getProductId())
				.productName(productName)
				.quantity(orderRequest.getQuantity())
				.amount(totalAmount)
				.build();
		
		orderRequest.setAmount(totalAmount);
		orderRequest.setProductStatus(productStatus);
		return new ProductEvent(productRequest, productStatus, orderRequest);
	}

	@Override
	@Transactional
	public void handleProductCancellation(ProductEvent productEvent) {
		Optional<Product> optional = productRepository.findById(productEvent.getProductRequest().getProductId());
		if(optional.isPresent()) {
			Product product = optional.get();
			product.setAvailabilityCount(product.getAvailabilityCount() + productEvent.getProductRequest().getQuantity());
			productRepository.save(product);
		}
	}

}
