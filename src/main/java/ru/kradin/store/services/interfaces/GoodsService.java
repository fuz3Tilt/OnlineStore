package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.validators.GoodsValidator;

import java.io.IOException;
import java.util.List;

public interface GoodsService {
    public List<GoodsValidator> getAvailableCatalogGoodsByCatalogId(int id);

    public List<GoodsValidator> getAllCatalogGoodsByCatalogId(int id);

    public List<GoodsValidator> getAllUnavailableGoods();

    public GoodsValidator getAvailableGoodsById(int id) throws RuntimeException;

    public GoodsValidator getGoodsById(int id) throws RuntimeException;

    public void saveGoods(GoodsValidator goodsValidator) throws IOException, RuntimeException, NameAlreadyUseException;

    public void deleteGoodsById(int id) throws RuntimeException, IOException;
}
