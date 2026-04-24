package com.learn.backend.Order.dto;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,

        Long productId,

        Integer Quantity,

        BigDecimal totalAmount,

        String status
) {
}
