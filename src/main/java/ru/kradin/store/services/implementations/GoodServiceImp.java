package ru.kradin.store.services.implementations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.DTOs.GoodDTO;
import ru.kradin.store.DTOs.GoodEditDTO;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.models.Good;
import ru.kradin.store.repositories.CatalogRepository;
import ru.kradin.store.repositories.GoodRepository;
import ru.kradin.store.services.interfaces.AdminGoodService;

import java.util.List;

@Service
public class GoodServiceImp implements AdminGoodService {
    private static final Logger log = LoggerFactory.getLogger(GoodServiceImp.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    @Transactional
    public List<GoodDTO> getAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Good> goodList = goodRepository.findAll(sort);
        List<GoodDTO> goodDTOList = modelMapper.map(goodList, new TypeToken<List<GoodDTO>>() {}.getType());
        return goodDTOList;
    }

    @Override
    @Transactional
    public List<GoodDTO> getByCatalogId(Long catalogId) {
        List<Good> goodList = goodRepository.findByCatalog_IdOrderByNameAsc(catalogId);
        List<GoodDTO> goodDTOList = modelMapper.map(goodList, new TypeToken<List<GoodDTO>>() {}.getType());
        return goodDTOList;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void create(GoodCreateDTO goodCreateDTO) {
        Good good = new Good(
                goodCreateDTO.getName(),
                goodCreateDTO.getDescription(),
                goodCreateDTO.getImageURL(),
                goodCreateDTO.getInStock(),
                goodCreateDTO.getPrice(),
                getCatalog(goodCreateDTO.getCatalogId())
        );
        goodRepository.save(good);
        log.info("Good {} saved.",goodCreateDTO.getName());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(GoodEditDTO goodEditDTO) {
        Good good = goodRepository.findById(goodEditDTO.getId()).get();
        good.setName(goodEditDTO.getName());
        good.setDescription(goodEditDTO.getDescription());
        good.setInStock(goodEditDTO.getInStock());
        good.setPrice(goodEditDTO.getPrice());
        good.setImageURL(goodEditDTO.getImageURL());
        goodRepository.save(good);
        log.info("Good {} updated.",good.getName());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long goodId) {
        Good good = goodRepository.findById(goodId).get();
        goodRepository.delete(good);
        log.info("Good {} deleted.",good.getName());
    }

    private Catalog getCatalog(Long catalogId) {
        return catalogRepository.findById(catalogId).get();
    }
}
