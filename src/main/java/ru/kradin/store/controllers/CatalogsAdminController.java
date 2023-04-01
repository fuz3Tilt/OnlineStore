package ru.kradin.store.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.services.interfaces.CatalogService;
import ru.kradin.store.services.interfaces.GoodsService;
import ru.kradin.store.utils.ImageErrorsUtil;
import ru.kradin.store.validators.CatalogValidator;

import java.io.IOException;

@Controller
@RequestMapping("/store/admin/catalogs")
public class CatalogsAdminController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping
    public String catalogs(Model model){
        model.addAttribute("catalogList",catalogService.getAllCatalogs());
        return "admin/catalog/catalogs";
    }

    @GetMapping("/new")
    public String newCatalog(@ModelAttribute("catalogValidator") CatalogValidator catalogValidator){
        return "admin/catalog/new";
    }

    @PostMapping
    public String addNewCatalog(@ModelAttribute("catalogValidator") @Valid CatalogValidator catalogValidator
                                ,BindingResult bindingResult) throws IOException {

        ImageErrorsUtil.addErrorsIfExist(catalogValidator.getImageToUpload(),bindingResult,"catalogValidator");

        if (bindingResult.hasErrors())
            return "admin/catalog/new";

        try {
            catalogService.saveCatalog(catalogValidator);
        } catch (NameAlreadyUseException e) {
            FieldError error = new FieldError("catalogValidator", "name", "Название каталога уже используется");
            bindingResult.addError(error);
            return "admin/catalog/new";
        }

        return "redirect:/store/admin/catalogs";
    }

    @GetMapping("/{catalog_id}")
    public String catalogGoods(Model model, @PathVariable("catalog_id") int id){
        model.addAttribute("catalog_id", id);
        model.addAttribute("goodsList", goodsService.getAllCatalogGoodsByCatalogId(id));
        return "admin/catalog/catalog-goods";
    }

    @GetMapping("/{catalog_id}/edit")
    public String editCatalog(Model model, @PathVariable("catalog_id") int id){
        model.addAttribute("catalogValidator", catalogService.getCatalogById(id));
        return "admin/catalog/edit";
    }

    @PatchMapping("/{catalog_id}")
    public String updateCatalog(@ModelAttribute("catalogValidator") @Valid CatalogValidator catalogValidator
                                ,BindingResult bindingResult
                                ,@PathVariable("catalog_id") int id) throws IOException {

        catalogValidator.setId(id);

        if(!catalogValidator.getImageToUpload().isEmpty())
            ImageErrorsUtil.addErrorsIfExist(catalogValidator.getImageToUpload(),bindingResult,"catalogValidator");

        if(bindingResult.hasErrors())
            return "admin/catalog/edit";

        try {
            catalogService.saveCatalog(catalogValidator);
        } catch (NameAlreadyUseException e) {
            FieldError error = new FieldError("catalogValidator", "name", "Название каталога уже используется");
            bindingResult.addError(error);
            return "admin/catalog/edit";
        }

        return "redirect:/store/admin/catalogs";
    }

    @DeleteMapping("/{catalog_id}")
    public String deleteCatalog(@PathVariable("catalog_id") int id) throws IOException {
        catalogService.deleteCatalogById(id);
        return "redirect:/store/admin/catalogs";
    }
}
