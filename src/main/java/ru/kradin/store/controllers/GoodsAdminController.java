package ru.kradin.store.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.services.interfaces.GoodsService;
import ru.kradin.store.utils.ImageErrorsUtil;
import ru.kradin.store.validators.GoodsValidator;

import java.io.IOException;

@Controller
@RequestMapping("/store/admin/goods")
public class GoodsAdminController {

    @Autowired
    GoodsService goodsService;

    @GetMapping("/unavailable")
    public String unavailableGoods(Model model){
        model.addAttribute("goodsList", goodsService.getAllUnavailableGoods());
        return "admin/goods/unavailable";
    }

    @GetMapping("/new")
    public String newGoods(@ModelAttribute("goodsValidator") GoodsValidator goodsValidator, @RequestParam("catalog") int id){
        goodsValidator.setCatalogId(id);
        return "admin/goods/new";
    }

    @PostMapping
    public String addNewGoods(@ModelAttribute("goodsValidator") @Valid GoodsValidator goodsValidator,
                              BindingResult bindingResult) throws IOException {

        ImageErrorsUtil.addErrorsIfExist(goodsValidator.getImageToUpload(),bindingResult,"goodsValidator");

        if(bindingResult.hasErrors())
            return "admin/goods/new";

        try {
            goodsService.saveGoods(goodsValidator);
        } catch (NameAlreadyUseException e) {
            FieldError error = new FieldError("goodsValidator","name", "Название товара уже используется");
            bindingResult.addError(error);
        }

        return "redirect:/store/admin/catalogs/"+goodsValidator.getCatalogId();
    }

    @GetMapping("/{goods_id}/edit")
    public String editGoods(Model model,
                            @PathVariable("goods_id") int id,
                            @RequestParam("from") String url){
        model.addAttribute("from", url);
        model.addAttribute("goodsValidator",goodsService.getGoodsById(id));
        return "admin/goods/edit";
    }

    @PatchMapping("/{goods_id}")
    public String updateGoods(@ModelAttribute("goodsValidator") @Valid GoodsValidator goodsValidator,
                              BindingResult bindingResult,
                              @PathVariable("goods_id") int id,
                              @RequestParam("from") String url,
                              Model model) throws IOException {

        goodsValidator.setId(id);
        model.addAttribute("from", url);

        if(!goodsValidator.getImageToUpload().isEmpty())
            ImageErrorsUtil.addErrorsIfExist(goodsValidator.getImageToUpload(),bindingResult,"goodsValidator");

        if(bindingResult.hasErrors())
            return "admin/goods/edit";

        try {
            goodsService.saveGoods(goodsValidator);
        } catch (NameAlreadyUseException e) {
            FieldError error = new FieldError("goodsValidator","name", "Название товара уже используется");
            bindingResult.addError(error);
            return "admin/goods/edit";
        }

        return "redirect:"+url;
    }

    @DeleteMapping("/{goods_id}")
    public String deleteGoods(@PathVariable("goods_id") int id,
                              @RequestParam("from") String url) throws IOException{
        goodsService.deleteGoodsById(id);
        return "redirect:"+url;
    }
}