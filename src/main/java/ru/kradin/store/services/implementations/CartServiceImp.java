package ru.kradin.store.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.CartDTO;
import ru.kradin.store.DTOs.GoodQuantityCreateDTO;
import ru.kradin.store.models.Cart;
import ru.kradin.store.models.Good;
import ru.kradin.store.models.CartGoodQuantity;
import ru.kradin.store.repositories.CartGoodQuantityRepository;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.GoodRepository;
import ru.kradin.store.services.interfaces.CartService;
import ru.kradin.store.services.interfaces.CurrentSessionService;

import java.util.List;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrentSessionService currentSessionService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartGoodQuantityRepository cartGoodQuantityRepository;
    @Autowired
    private GoodRepository goodRepository;

    @Override
    @PreAuthorize("isAuthenticated()")
    public CartDTO getCurtOfCurrentUser() {
        Cart cart = cartRepository.findByUser(currentSessionService.getUser());
        List<CartGoodQuantity> cartGoodQuantityList = cartGoodQuantityRepository.findByCart(cart);
        cart.setGoodQuantityList(cartGoodQuantityList);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        return cartDTO;
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void changeGoodQuantity(GoodQuantityCreateDTO goodQuantityCreateDTO) {
        Cart cart = cartRepository.findByUser(currentSessionService.getUser());
        Good good = goodRepository.findById(goodQuantityCreateDTO.getGoodId()).get();
        List<CartGoodQuantity> goodQuantityList = cart.getGoodQuantityList();
        CartGoodQuantity cartGoodQuantity = goodQuantityList.stream()
                .filter(goodQuantity -> goodQuantity.getGood().equals(good))
                .findFirst()
                .orElseGet(() -> {
                    CartGoodQuantity newCartGoodQuantity = new CartGoodQuantity(good, 0L, cart);
                    goodQuantityList.add(newCartGoodQuantity);
                    return newCartGoodQuantity;
                });
        if (goodQuantityCreateDTO.getQuantity() > 0) {
            cartGoodQuantity.setQuantity(goodQuantityCreateDTO.getQuantity());
        } else {
            goodQuantityList.remove(cartGoodQuantity);
        }
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void emptyCart() {
        Cart cart = cartRepository.findByUser(currentSessionService.getUser());
        cart.getGoodQuantityList().clear();
        cartRepository.save(cart);
    }
}
