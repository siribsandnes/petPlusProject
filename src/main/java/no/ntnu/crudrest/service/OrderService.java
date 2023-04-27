package no.ntnu.crudrest.service;

import no.ntnu.crudrest.models.Order;
import no.ntnu.crudrest.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(int orderId) {
        return orderRepository.findById(orderId);
    }
}