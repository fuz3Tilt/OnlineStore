package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.DTOs.GoodEditDTO;

public interface AdminGoodService extends UserGoodService{
    public void create(GoodCreateDTO goodCreateDTO);
    public void update(GoodEditDTO goodEditDTO);
    public void delete(Long goodId);
}
