package ru.kradin.store.services.implementations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.GoodQuantityCreateDTO;
import ru.kradin.store.DTOs.OrderCreateDTO;
import ru.kradin.store.DTOs.OrderDTO;
import ru.kradin.store.enums.Status;
import ru.kradin.store.models.*;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.GoodRepository;
import ru.kradin.store.repositories.OrderGoodQuantityRepository;
import ru.kradin.store.repositories.OrderRepository;
import ru.kradin.store.services.interfaces.AdminOrderService;
import ru.kradin.store.services.interfaces.CurrentSessionService;
import ru.kradin.store.services.interfaces.UserOrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImp implements AdminOrderService, UserOrderService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrentSessionService currentSessionService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private OrderGoodQuantityRepository orderGoodQuantityRepository;

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<OrderDTO> getOrders() {
        List<Order> orderList = orderRepository.findByUser(currentSessionService.getUser());
        for (Order order:orderList) {
            List<OrderGoodQuantity> orderGoodQuantityList = orderGoodQuantityRepository.findByOrder(order);
            order.setGoodQuantityList(orderGoodQuantityList);
        }
        List<OrderDTO> orderDTOList = modelMapper.map(orderList, new TypeToken<List<OrderDTO>>() {}.getType());
        return orderDTOList;
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void create(OrderCreateDTO orderCreateDTO) {
        Order order = new Order(
                currentSessionService.getUser(),
                orderCreateDTO.getAddress(),
                orderCreateDTO.getPostalCode(),
                orderCreateDTO.getMessage(),
                LocalDateTime.now()
        );
        order = orderRepository.save(order);

        Cart cart = cartRepository.findByUser(currentSessionService.getUser());
        List<OrderGoodQuantity> orderGoodQuantityList = new ArrayList<>();
        for (CartGoodQuantity cartGoodQuantity : cart.getGoodQuantityList()) {
            OrderGoodQuantity orderGoodQuantity = new OrderGoodQuantity(
                    cartGoodQuantity.getGood(),
                    cartGoodQuantity.getQuantity(),
                    order
            );
            orderGoodQuantityList.add(orderGoodQuantity);
        }

        order.setGoodQuantityList(orderGoodQuantityList);
        orderRepository.save(order);

        cart.getGoodQuantityList().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void create(OrderCreateDTO orderCreateDTO, GoodQuantityCreateDTO goodQuantityCreateDTO) {
        Order order = new Order(
                currentSessionService.getUser(),
                orderCreateDTO.getAddress(),
                orderCreateDTO.getPostalCode(),
                orderCreateDTO.getMessage(),
                LocalDateTime.now()
        );
        order = orderRepository.save(order);

        List<OrderGoodQuantity> orderGoodQuantityList = new ArrayList<>();
        Good good = goodRepository.findById(goodQuantityCreateDTO.getGoodId()).get();
        OrderGoodQuantity orderGoodQuantity = new OrderGoodQuantity(
                good,
                goodQuantityCreateDTO.getQuantity(),
                order
        );
        orderGoodQuantityList.add(orderGoodQuantity);

        order.setGoodQuantityList(orderGoodQuantityList);
        orderRepository.save(order);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OrderDTO> getAll() {
        List<Order> orderList = orderRepository.findAll();
        for (Order order:orderList) {
            List<OrderGoodQuantity> orderGoodQuantityList = orderGoodQuantityRepository.findByOrder(order);
            order.setGoodQuantityList(orderGoodQuantityList);
        }
        List<OrderDTO> orderDTOList = modelMapper.map(orderList, new TypeToken<List<OrderDTO>>() {}.getType());
        return orderDTOList;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setStatus(Status status, Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        if (status.equals(Status.COMPLETED))
            order.setClosedAt(LocalDateTime.now());

        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setTrackCode(String trackCode, Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.setTrackCode(trackCode);
        orderRepository.save(order);
    }
}
