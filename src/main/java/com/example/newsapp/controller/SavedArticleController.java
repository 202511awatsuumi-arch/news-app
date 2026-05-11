package com.example.newsapp.controller;

import com.example.newsapp.form.ArticleSaveForm;
import com.example.newsapp.service.SavedArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SavedArticleController {
    private final SavedArticleService savedArticleService;

    public SavedArticleController(SavedArticleService savedArticleService) {
        this.savedArticleService = savedArticleService;
    }

    @PostMapping("/articles/save")
    public String save(ArticleSaveForm form) {
        savedArticleService.save(form);
        return "redirect:/saved";
    }

    @GetMapping("/saved")
    public String saved(Model model) {
        model.addAttribute("savedArticles", savedArticleService.findAll());
        return "saved";
    }

    @PostMapping("/saved/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        savedArticleService.deleteById(id);
        return "redirect:/saved";
    }
}
