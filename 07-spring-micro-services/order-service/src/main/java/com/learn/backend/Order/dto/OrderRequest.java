package com.learn.backend.Order.dto;

public record OrderRequest(
        Long productId,

        Integer quantity
) {
}
