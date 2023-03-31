package ru.kradin.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/store/admin/goods")
public class GoodsAdminController {

    @GetMapping("/unavailable")
    public String unavailableGoods(){
        return null;
    }

    @GetMapping("/new")
    public String newGoods(){
        return null;
    }

    @GetMapping("/{goods_id}")
    public String goods(){
        return null;
    }

    @GetMapping("/{goods_id}/edit")
    public String editGoods(){
        return null;
    }

    @PostMapping
    public String addNewGoods(){
        return null;
    }

    @PatchMapping("/{goods_id}")
    public String updateGoods(){
        return null;
    }

    @DeleteMapping("/{goods_id}")
    public String deleteGoods(){
        return null;
    }
}