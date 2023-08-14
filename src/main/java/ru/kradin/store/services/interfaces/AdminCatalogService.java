package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CatalogCreateDTO;
import ru.kradin.store.DTOs.CatalogEditDTO;

/**
 * Uses for CRUD operations with catalogs
 */
public interface AdminCatalogService extends UserCatalogService {
    public void create(CatalogCreateDTO catalogCreateDTO);
    public void update(CatalogEditDTO catalogEditDTO);
    public void delete(Long catalogId);
}
