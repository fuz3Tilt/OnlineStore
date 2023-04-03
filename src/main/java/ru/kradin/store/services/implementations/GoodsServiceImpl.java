package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.kradin.store.enums.Status;
import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.models.Goods;
import ru.kradin.store.repositories.CatalogRepository;
import ru.kradin.store.repositories.GoodsRepository;
import ru.kradin.store.services.interfaces.GoodsService;
import ru.kradin.store.services.interfaces.ImagesManagerService;
import ru.kradin.store.validators.GoodsValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private ImagesManagerService imagesManager;


    @Override
    public List<GoodsValidator> getAvailableCatalogGoodsByCatalogId(int id) {
        List<Goods> goodsList = goodsRepository.findByCatalog_IdAndStatusOrderByNameAsc(id, Status.AVAILABLE);
        List<GoodsValidator> goodsValidatorList= new ArrayList<>();
        for(Goods goods:goodsList)
            goodsValidatorList.add(goodsToGoodsValidator(goods));
        return goodsValidatorList;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GoodsValidator> getAllCatalogGoodsByCatalogId(int id) {
        List<Goods> goodsList = goodsRepository.findByCatalog_IdOrderByNameAsc(id);
        List<GoodsValidator> goodsValidatorList= new ArrayList<>();
        for(Goods goods:goodsList)
            goodsValidatorList.add(goodsToGoodsValidator(goods));
        return goodsValidatorList;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GoodsValidator> getAllUnavailableGoods() {
        List<Goods> goodsList = goodsRepository.findByStatusOrderByNameAsc(Status.UNAVAILABLE);
        List<GoodsValidator> goodsValidatorList= new ArrayList<>();
        for(Goods goods:goodsList)
            goodsValidatorList.add(goodsToGoodsValidator(goods));
        return goodsValidatorList;
    }

    @Override
    public GoodsValidator getAvailableGoodsById(int id) throws RuntimeException{
        Goods goods = goodsRepository.findByIdAndStatus(id,Status.AVAILABLE);
        if (goods == null)
            throw new RuntimeException("Could not find the goods.");
        return goodsToGoodsValidator(goods);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GoodsValidator getGoodsById(int id) {
        Goods goods = goodsRepository.findById(id).orElse(null);
        if(goods==null)
            throw new RuntimeException("Could not find the goods.");

        return goodsToGoodsValidator(goods);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveGoods(GoodsValidator goodsValidator) throws IOException, RuntimeException, NameAlreadyUseException {
        Catalog catalog = catalogRepository.findById(goodsValidator.getCatalogId()).orElse(null);
        if(catalog == null)
            throw new RuntimeException("Could not find the catalog to puts the goods inside.");


        if(goodsValidator.getId()==0&&goodsValidator.getImageToUpload().isEmpty())
            throw new RuntimeException("Goods must have an image to upload.");
        //create
        if (goodsValidator.getId() == 0 && !goodsValidator.getImageToUpload().isEmpty()) {
            Goods goods = new Goods();

            if(!goodsRepository.findByName(goodsValidator.getName()).isEmpty())
                throw new NameAlreadyUseException();

            goods.setName(goodsValidator.getName());
            goods.setDescription(goodsValidator.getDescription());
            goods.setStatus(goodsValidator.getStatus());
            goods.setPrice(goodsValidator.getPrice());
            goods.setCatalog(catalog);

            try {
            goods.setImageName(imagesManager.save(goodsValidator.getImageToUpload()));
            } catch(IOException e){
                log.error("Could not save image to disk. {}", e.getMessage());
                throw e;
            }
            goodsRepository.save(goods);
            log.info("Goods {} saved in database.", goods.getName());
        }
        //update
        if(goodsValidator.getId()!=0){
            Goods goods = goodsRepository.findById(goodsValidator.getId()).orElse(null);
            if(goods == null)
                throw new RuntimeException("Could not find the goods to update.");

            if(!goods.getName().equals(goodsValidator.getName())) {
                if(!goodsRepository.findByName(goodsValidator.getName()).isEmpty())
                    throw new NameAlreadyUseException();
            }

            goods.setName(goodsValidator.getName());
            goods.setDescription(goodsValidator.getDescription());
            goods.setStatus(goodsValidator.getStatus());
            goods.setPrice(goodsValidator.getPrice());
            goods.setCatalog(catalog);

            if(!goodsValidator.getImageToUpload().isEmpty()){
                try {
                    String brokerToDelete = goods.getImageName();
                    goods.setImageName(imagesManager.save(goodsValidator.getImageToUpload()));
                    imagesManager.delete(brokerToDelete);
                } catch (IOException e) {
                    log.error("Could not update image. {}", e.getMessage());
                    throw e;
                }
            }
            goodsRepository.save(goods);
            log.info("Goods {} updated.", goods.getName());
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteGoodsById(int id) throws IOException, RuntimeException {
        Goods goods = goodsRepository.findById(id).orElse(null);
        if(goods == null)
            throw new RuntimeException("Could not find the goods to delete.");

        try{
            imagesManager.delete(goods.getImageName());
        } catch (IOException e){
            log.error(e.getMessage()+" {}.", goods.getImageName());
            throw e;
        }
        goodsRepository.delete(goods);
        log.info("Goods {} deleted.", goods.getName());
    }

    private GoodsValidator goodsToGoodsValidator(Goods goods){
        GoodsValidator goodsValidator = new GoodsValidator();
        goodsValidator.setId(goods.getId());
        goodsValidator.setName(goods.getName());
        goodsValidator.setDescription(goods.getDescription());
        goodsValidator.setPrice(goods.getPrice());
        goodsValidator.setStatus(goods.getStatus());
        goodsValidator.setCatalogId(goods.getCatalog().getId());
        goodsValidator.setImageName(goods.getImageName());
        return goodsValidator;
    }
}
