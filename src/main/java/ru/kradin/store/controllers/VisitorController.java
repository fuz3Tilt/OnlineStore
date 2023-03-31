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
        return null;
    }

    @GetMapping("/catalogs/{catalog_id}")
    public String catalog(){
        return null;
    }

    @GetMapping("/goods/{goods_id}")
    public String goods(){
        return null;
    }
}
