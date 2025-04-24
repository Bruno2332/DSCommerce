package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.DTO.OrderDTO;
import com.devsuperior.dscommerce.DTO.OrderItemDTO;
import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.OrderFactory;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    private Order order;
    private Product product;
    private OrderItem orderItem;
    private OrderItemDTO orderItemDTO;
    private OrderDTO orderDTO;
    private Long existsId;
    private Long userClient;
    private Long nonExistsId;

    @BeforeEach
    void setUp() throws Exception{
        order = OrderFactory.createOrder();
        product = ProductFactory.createProduct();
        orderItem = OrderFactory.createOrderItem(order, product);
        orderItemDTO = OrderFactory.createOrderItemDTO(orderItem);
        orderDTO = OrderFactory.createOrderDTO(order, orderItemDTO);
        existsId = 1L;
        userClient = 2L;
        nonExistsId = 3L;

        when(userService.authenticated()).thenReturn(order.getClient());
        when(repository.findById(existsId)).thenReturn(Optional.of(order));
        when(repository.findById(nonExistsId)).thenReturn(Optional.empty());
        doNothing().when(authService).validateSelfOrAdmin(order.getClient().getId());
        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(userClient);
        when(repository.save(ArgumentMatchers.any())).thenReturn(order);
        when(productRepository.getReferenceById(product.getId())).thenReturn(product);
        when(orderItemRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

    }

    @Test
    public void findByIdShuldReturnOrderWhenExistsIdAndValidateUser(){
        OrderDTO result = service.findById(existsId);

        Assertions.assertEquals(order.getClient().getName(), result.getClient().getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistsId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistsId);
        });
    }

    @Test
    public void findByIdShouldThrowForbiddenExcptionWhenUserNotAdmin(){
        order.getClient().setId(userClient);
        Assertions.assertThrows(ForbiddenException.class, () -> {
            service.findById(existsId);
        });
    }

    @Test
    public void insertShouldInsertOrderDtoWhenValidUser(){
        OrderDTO result = service.insert(orderDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(order.getClient().getId(), result.getClient().getId());
        Assertions.assertEquals(OrderStatus.WAITING_PAYMENT, result.getStatus());
        Assertions.assertFalse(result.getItems().isEmpty());
    }


}
