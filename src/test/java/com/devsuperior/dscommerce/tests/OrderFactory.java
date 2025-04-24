package com.devsuperior.dscommerce.tests;


import com.devsuperior.dscommerce.DTO.OrderDTO;
import com.devsuperior.dscommerce.DTO.OrderItemDTO;
import com.devsuperior.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder(){
        return new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, UserFactory.createUserAdmin(), new Payment(1L, Instant.now(), new Order()));
    }


    public static OrderItem createOrderItem(Order order, Product product){
        OrderItemPK pk = new OrderItemPK();
        pk.setOrder(order);
        pk.setProduct(product);

        return new OrderItem(pk.getOrder(), pk.getProduct(), 2, 300.0);
    }


    public static OrderItemDTO createOrderItemDTO(OrderItem entity) {
        return new OrderItemDTO(entity);
    }

    public static OrderDTO createOrderDTO(Order entity, OrderItemDTO itemDTO){
        OrderDTO orderDTO = new OrderDTO(entity);
        orderDTO.getItems().add(itemDTO);
        return orderDTO;
    }
}
