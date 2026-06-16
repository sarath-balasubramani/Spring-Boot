package com.employee.management.service;

import com.employee.management.dto.EmployeeResponse;
import com.employee.management.dto.OrderItemDTO;
import com.employee.management.dto.OrderResponse;
import com.employee.management.model.*;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final EmployeeRepository employeeRepository;
    private final CartService cartService;
    //    private final Order order;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String employeeId) {

        // Validate the Cart Item
        List<CartItem> cartItems = cartService.getAllItm(employeeId);
        if (cartItems.isEmpty()){
            return Optional.empty();
        }

        // Validate the user
        Optional<Employee> empOpt = employeeRepository.findById(Long.valueOf(employeeId));
        Employee emp = empOpt.get();

        // Calculate totalPrice
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // reduce("Initial value", "Operation to perform")

        // Create order
        Order order = new Order();
        order.setEmployee(emp);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream().map(item -> new OrderItem(
                null,
                item.getProduct(),
                item.getQuantity(),
                item.getPrice(),
                order
        )).toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        //Clear the cart
        cartService.clearCart(employeeId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

//    private OrderResponse mapToOrderResponse(Order savedOrder){
//        return new OrderResponse(
//                savedOrder.getId(),
//                savedOrder.getTotalAmount(),
//                savedOrder.getStatus(),
//                savedOrder.getItems().stream().map(orderItem -> new OrderItem(
//                        orderItem.getId(),
//                        orderItem.getProduct().getProdId(),
//                        orderItem.getQuantity(),
//                        orderItem.getPrice(),
//                        orderItem.getPrice().multiply(BigDecimal.valueOf(100)))).toList(),
//                savedOrder.getCreatedAt()
//        );
//    }
private OrderResponse mapToOrderResponse(Order savedOrder){

    EmployeeResponse employeeResponse = new EmployeeResponse(
            savedOrder.getEmployee().getEmpid(),
            savedOrder.getEmployee().getFirstName(),
            savedOrder.getEmployee().getEmail()
    );

    List<OrderItemDTO> items = savedOrder.getItems().stream()
            .map(orderItem -> new OrderItemDTO(
            ))
            .toList();

    return new OrderResponse(
            savedOrder.getId(),
            employeeResponse,
            savedOrder.getTotalAmount(),
            savedOrder.getStatus(),
            items,
            savedOrder.getCreatedAt()
    );
}
}