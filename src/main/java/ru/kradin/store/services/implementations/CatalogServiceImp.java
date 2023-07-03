package ru.kradin.store.services.implementations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kradin.store.DTOs.CatalogCreateDTO;
import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.CatalogEditDTO;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.models.Good;
import ru.kradin.store.repositories.CatalogRepository;
import ru.kradin.store.repositories.GoodRepository;
import ru.kradin.store.services.interfaces.AdminCatalogService;
import ru.kradin.store.services.interfaces.ImageService;

import java.io.IOException;
import java.util.List;

@Service
public class CatalogServiceImp implements AdminCatalogService {
    private static final Logger log = LoggerFactory.getLogger(CatalogServiceImp.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private ImageService imageService;
    @Value("${store.imagesURL}")
    private String imagesURL;
    private static final String IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND";

    @Override
    public List<CatalogDTO> getAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Catalog> catalogList = catalogRepository.findAll(sort);
        List<CatalogDTO> catalogDTOList = modelMapper.map(catalogList, new TypeToken<List<CatalogDTO>>() {}.getType());
        return catalogDTOList;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void create(CatalogCreateDTO catalogCreateDTO) {
        Catalog catalog = new Catalog(
                catalogCreateDTO.getName(),
                saveAndGetURL(catalogCreateDTO.getImage())
        );
        catalogRepository.save(catalog);
        log.info("Catalog {} saved.",catalog.getName());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(CatalogEditDTO catalogEditDTO) {
        Catalog catalog = catalogRepository.findById(catalogEditDTO.getId()).get();
        catalog.setName(catalogEditDTO.getName());
        if (catalogEditDTO.getImage() != null && !catalogEditDTO.getImage().isEmpty()) {
            deleteImage(catalog.getImageURL());
            catalog.setImageURL(saveAndGetURL(catalogEditDTO.getImage()));
        }
        catalogRepository.save(catalog);
        log.info("Catalog {} updated.", catalog.getName());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).get();
        deleteImage(catalog.getImageURL());
        deleteGoodsImages(catalogId);
        catalogRepository.delete(catalog);
        log.info("Catalog {} deleted.",catalog.getName());
    }

    private String saveAndGetURL(MultipartFile image) {
        String imageURL = "";
        try {
            imageURL = imagesURL + imageService.save(image);
        } catch (IOException e) {
            imageURL = imagesURL + IMAGE_NOT_FOUND;
            log.error("Image error then tried to save.");
        }
        return imageURL;
    }

    private void deleteImage(String oldImageURL) {
        try {
            if (!oldImageURL.equals(imagesURL+IMAGE_NOT_FOUND))
                imageService.delete(oldImageURL.substring(imagesURL.length()));
        } catch (IOException e) {
            log.error("Image error the tried to delete.");
        }
    }

    private void deleteGoodsImages(Long catalogId) {
        List<Good> goodList = goodRepository.findByCatalog_IdOrderByNameAsc(catalogId);
        goodList.forEach(good -> deleteImage(good.getImageURL()));
    }
}
