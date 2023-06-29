package ru.kradin.store.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.AdditionalPriceCreateDTO;
import ru.kradin.store.models.AdditionalPrice;
import ru.kradin.store.models.Order;
import ru.kradin.store.repositories.AdditionalPriceRepository;
import ru.kradin.store.repositories.OrderRepository;
import ru.kradin.store.services.interfaces.AdditionalPriceService;

@Service
public class AdditionalPriceServiceImp implements AdditionalPriceService {
    @Autowired
    private AdditionalPriceRepository additionalPriceRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void add(AdditionalPriceCreateDTO additionalPriceCreateDTO) {
        Order order = orderRepository.findById(additionalPriceCreateDTO.getOrderId()).get();
        AdditionalPrice additionalPrice = new AdditionalPrice(
                additionalPriceCreateDTO.getReason(),
                additionalPriceCreateDTO.getPrice(),
                order
        );
        additionalPriceRepository.save(additionalPrice);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void remove(Long id) {
        additionalPriceRepository.deleteById(id);
    }
}
