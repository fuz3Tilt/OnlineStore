package ru.kradin.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class VisitorController {

    @GetMapping
    public String main(){
        return "redirect:/catalogs";
    }

    @GetMapping("/catalogs")
    public String allCatalogs(){
        return "visitor/catalogs";
    }

    @GetMapping("/catalogs/{catalog_id}")
    public String catalog(){
        return "visitor/catalog-goods";
    }

    @GetMapping("/goods/{goods_id}")
    public String goods(){
        return "visitor/goods";
    }
}
