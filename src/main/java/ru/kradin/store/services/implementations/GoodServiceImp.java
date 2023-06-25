package ru.kradin.store.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.DTOs.GoodDTO;
import ru.kradin.store.DTOs.GoodEditDTO;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.models.Good;
import ru.kradin.store.repositories.CatalogRepository;
import ru.kradin.store.repositories.GoodRepository;
import ru.kradin.store.services.interfaces.GoodService;
import ru.kradin.store.services.interfaces.ImageService;

import java.io.IOException;
import java.util.List;

@Service
public class GoodServiceImp implements GoodService {
    private static final Logger log = LoggerFactory.getLogger(GoodServiceImp.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private ImageService imageService;
    @Value("${store.imagesURL}")
    static String imagesURL;
    private static final String IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND";
    @Override
    public List<GoodDTO> getAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Good> goodList = goodRepository.findAll(sort);
        List<GoodDTO> goodDTOList = modelMapper.map(goodList, new TypeToken<List<GoodDTO>>() {}.getType());
        return goodDTOList;
    }

    @Override
    public List<GoodDTO> getByCatalogId(Long catalogId) {
        List<Good> goodList = goodRepository.findByCatalog_IdOrderByNameAsc(catalogId);
        List<GoodDTO> goodDTOList = modelMapper.map(goodList, new TypeToken<List<GoodDTO>>() {}.getType());
        return goodDTOList;
    }

    @Override
    @Transactional
    public void create(GoodCreateDTO goodCreateDTO) {
        Good good = new Good(
                goodCreateDTO.getName(),
                goodCreateDTO.getDescription(),
                saveAndGetURL(goodCreateDTO.getImage()),
                goodCreateDTO.getInStock(),
                goodCreateDTO.getPrice(),
                getCatalog(goodCreateDTO.getCatalog())
        );
        goodRepository.save(good);
        log.info("Good {} saved.",goodCreateDTO.getName());
    }

    @Override
    @Transactional
    public void update(GoodEditDTO goodEditDTO) {
        Good good = goodRepository.findById(goodEditDTO.getId()).get();
        good.setName(goodEditDTO.getName());
        good.setDescription(goodEditDTO.getDescription());
        good.setInStock(goodEditDTO.getInStock());
        good.setPrice(goodEditDTO.getPrice());
        if (goodEditDTO.getImage() != null && !goodEditDTO.getImage().isEmpty()) {
            deleteImage(good.getImageURL());
            good.setImageURL(saveAndGetURL(goodEditDTO.getImage()));
        }
        goodRepository.save(good);
        log.info("Good {} updated.",good.getName());
    }

    @Override
    @Transactional
    public void delete(Long goodId) {
        Good good = goodRepository.findById(goodId).get();
        deleteImage(good.getImageURL());
        goodRepository.delete(good);
        log.info("Good {} deleted.",good.getName());
    }

    @Override
    public void deleteImagesByCatalogId(Long catalogId) {
        List<Good> goodList = goodRepository.findByCatalog_IdOrderByNameAsc(catalogId);
        goodList.forEach(good -> deleteImage(good.getImageURL()));
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

    private Catalog getCatalog(CatalogDTO catalogDTO) {
        return catalogRepository.findById(catalogDTO.getId()).get();
    }
}
