package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.DTOs.GoodEditDTO;

/**
 * Uses for CRUD operations with goods
 */
public interface AdminGoodService extends UserGoodService{
    public void create(GoodCreateDTO goodCreateDTO);
    public void update(GoodEditDTO goodEditDTO);
    public void delete(Long goodId);
}
