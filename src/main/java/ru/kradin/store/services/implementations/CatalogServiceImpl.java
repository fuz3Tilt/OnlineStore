package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.models.Goods;
import ru.kradin.store.repositories.CatalogRepository;
import ru.kradin.store.repositories.GoodsRepository;
import ru.kradin.store.services.interfaces.CatalogService;
import ru.kradin.store.services.interfaces.ImagesManagerService;
import ru.kradin.store.validators.CatalogValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    private static final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private ImagesManagerService imagesManager;
    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public List<CatalogValidator> getAllCatalogs() {
        List<Catalog> catalogList = catalogRepository.findByOrderByNameAsc();
        List<CatalogValidator> catalogValidatorList = new ArrayList<>();
        for(Catalog catalog:catalogList)
            catalogValidatorList.add(catalogToCatalogValidator(catalog));
        return catalogValidatorList;
    }

    @Override
    public CatalogValidator getCatalogById(int id) throws RuntimeException {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        if(catalog == null)
            throw new RuntimeException("Could not find the catalog.");

        return catalogToCatalogValidator(catalog);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveCatalog(CatalogValidator catalogValidator) throws IOException, RuntimeException, NameAlreadyUseException {

        if(catalogValidator.getId()==0&&catalogValidator.getImageToUpload().isEmpty())
            throw new RuntimeException("Catalog must have an image to upload.");
        //create
        if (catalogValidator.getId() == 0 && !catalogValidator.getImageToUpload().isEmpty()) {
            Catalog catalog = new Catalog();

            if(!catalogRepository.findByName(catalogValidator.getName()).isEmpty())
                throw new NameAlreadyUseException();

            catalog.setName(catalogValidator.getName());

            try {
            catalog.setImageName(imagesManager.save(catalogValidator.getImageToUpload()));
            } catch(IOException e){
                log.error("Could not save image to disk. {}", e.getMessage());
                throw e;
            }
            catalogRepository.save(catalog);
            log.info("Catalog {} saved in database.", catalog.getName());
        }
        //update
        if (catalogValidator.getId() != 0) {
            Catalog catalog = catalogRepository.findById(catalogValidator.getId()).orElse(null);
            if (catalog == null)
                throw new RuntimeException("Could not find the catalog to update.");

            if(!catalog.getName().equals(catalogValidator.getName())) {
                if (!catalogRepository.findByName(catalogValidator.getName()).isEmpty())
                    throw new NameAlreadyUseException();
            }

            catalog.setName(catalogValidator.getName());

            if (!catalogValidator.getImageToUpload().isEmpty()) {
                try {
                    String brokerToDelete = catalog.getImageName();
                    catalog.setImageName(imagesManager.save(catalogValidator.getImageToUpload()));
                    imagesManager.delete(brokerToDelete);
                } catch (IOException e) {
                    log.error("Could not update image. {}", e.getMessage());
                    throw e;
                }
                }
            catalogRepository.save(catalog);
            log.info("Catalog {} updated.", catalog.getName());
            }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCatalogById(int id) throws IOException, RuntimeException {
        Catalog catalog = catalogRepository.findById(id).orElse(null);
        if(catalog == null)
            throw new RuntimeException("Could not find the catalog to delete.");

        try {
            List<Goods> catalogGoodsList = goodsRepository.findByCatalog_IdOrderByNameAsc(catalog.getId());
            for(Goods goods:catalogGoodsList){
                imagesManager.delete(goods.getImageName());
            }
            imagesManager.delete(catalog.getImageName());
        } catch (IOException e) {
            log.error(e.getMessage()+" {}.", catalog.getImageName());
            throw e;
        }
        catalogRepository.delete(catalog);
        log.info("Catalog {} deleted.", catalog.getName());
    }

    private CatalogValidator catalogToCatalogValidator(Catalog catalog) {
        CatalogValidator catalogValidator = new CatalogValidator();
        catalogValidator.setId(catalog.getId());
        catalogValidator.setName(catalog.getName());
        catalogValidator.setImageName(catalog.getImageName());
        return catalogValidator;
    }
}
