package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CatalogCreateDTO;
import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.CatalogEditDTO;

import java.io.IOException;
import java.util.List;

public interface CatalogService {
    public List<CatalogDTO> getAll();
    public void create(CatalogCreateDTO catalogCreateDTO);
    public void update(CatalogEditDTO catalogEditDTO);
    public void delete(Long catalogId);
}
