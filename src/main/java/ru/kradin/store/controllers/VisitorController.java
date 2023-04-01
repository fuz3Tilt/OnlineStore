package ru.kradin.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kradin.store.services.interfaces.CatalogService;
import ru.kradin.store.services.interfaces.GoodsService;

@Controller
@RequestMapping("/")
public class VisitorController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    GoodsService goodsService;

    @GetMapping
    public String main(){
        return "redirect:/catalogs";
    }

    @GetMapping("/catalogs")
    public String allCatalogs(Model model){
        model.addAttribute("catalogList",catalogService.getAllCatalogs());
        return "visitor/catalogs";
    }

    @GetMapping("/catalogs/{catalog_id}")
    public String catalog(Model model, @PathVariable("catalog_id") int id){
        model.addAttribute("catalogName",catalogService.getCatalogById(id).getName());
        model.addAttribute("goodsList",goodsService.getAvailableCatalogGoodsByCatalogId(id));
        return "visitor/catalog-goods";
    }

    @GetMapping("/goods/{goods_id}")
    public String goods(Model model, @PathVariable("goods_id") int id){
        model.addAttribute("goodsValidator",goodsService.getAvailableGoodsById(id));
        return "visitor/goods";
    }
}
