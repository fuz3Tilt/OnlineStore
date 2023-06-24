package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CatalogCreateDTO;
import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.CatalogEditDTO;

import java.util.List;

public interface CatalogService {
    public List<CatalogDTO> getAllCatalogs();
    public void createCatalog(CatalogCreateDTO catalogCreateDTO);
    public void updateCatalog(CatalogEditDTO catalogEditDTO);
    public void deleteCatalog(Long catalogId);
}
