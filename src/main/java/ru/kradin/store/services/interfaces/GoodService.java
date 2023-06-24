package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.DTOs.GoodDTO;
import ru.kradin.store.DTOs.GoodEditDTO;

import java.util.List;

public interface GoodService {
    public List<GoodDTO> getAllGood();
    public void createGood(GoodCreateDTO goodCreateDTO);
    public void updateGood(GoodEditDTO goodEditDTO);
    public void deleteGood(Long goodId);
}
