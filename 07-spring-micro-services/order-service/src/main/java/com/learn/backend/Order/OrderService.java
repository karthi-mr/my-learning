package com.learn.backend.Order;

import com.learn.backend.Order.dto.OrderRequest;
import com.learn.backend.Order.dto.OrderResponse;
import com.learn.backend.Order.dto.ProductResponse;
import com.learn.backend.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        ProductResponse product = this.productClient.getProductById(orderRequest.productId()).getBody();

        assert product != null;

        if (product.stock() < orderRequest.quantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        BigDecimal totalAmount = product.price().multiply(BigDecimal.valueOf(orderRequest.quantity()));

        Order order = Order.builder()
                .productId(orderRequest.productId())
                .quantity(orderRequest.quantity())
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.CREATED)
                .build();
        Order savedOrder = this.orderRepository.save(order);

        this.productClient.reduceStock(orderRequest.productId(), orderRequest.quantity());
        savedOrder.setOrderStatus(OrderStatus.CONFIRMED);
        this.orderRepository.save(order);

        return this.toOrderResponse(savedOrder);
    }

    public OrderResponse getOrder(Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return this.toOrderResponse(order);
    }


    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getOrderStatus().name()
        );
    }
}
