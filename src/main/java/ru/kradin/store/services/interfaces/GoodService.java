package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.DTOs.GoodDTO;
import ru.kradin.store.DTOs.GoodEditDTO;

import java.io.IOException;
import java.util.List;

public interface GoodService {
    public List<GoodDTO> getAll();
    public List<GoodDTO> getByCatalogId(Long catalogId);
    public void create(GoodCreateDTO goodCreateDTO);
    public void update(GoodEditDTO goodEditDTO);
    public void delete(Long goodId);
    public void deleteImagesByCatalogId(Long catalogId);
}
