package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.validators.CatalogValidator;

import java.io.IOException;
import java.util.List;

public interface CatalogService {

    public List<CatalogValidator> getAllCatalogs();

    public CatalogValidator getCatalogById(int id) throws RuntimeException;

    public void saveCatalog(CatalogValidator catalogValidator) throws IOException, RuntimeException, NameAlreadyUseException;

    public void deleteCatalogById(int id) throws RuntimeException, IOException;
}
