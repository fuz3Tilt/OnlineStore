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
import ru.kradin.store.DTOs.CatalogCreateDTO;
import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.CatalogEditDTO;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.repositories.CatalogRepository;
import ru.kradin.store.services.interfaces.AdminCatalogService;

import java.util.List;

@Service
public class CatalogServiceImp implements AdminCatalogService {
    private static final Logger log = LoggerFactory.getLogger(CatalogServiceImp.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    @Transactional
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
                catalogCreateDTO.getImageURL()
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
        catalog.setImageURL(catalogEditDTO.getImageURL());
        catalogRepository.save(catalog);
        log.info("Catalog {} updated.", catalog.getName());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).get();
        catalogRepository.delete(catalog);
        log.info("Catalog {} deleted.",catalog.getName());
    }
}
