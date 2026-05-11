package com.example.newsapp.controller;

import com.example.newsapp.model.NewsArticle;
import com.example.newsapp.service.NewsService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final NewsService newsService;

    public HomeController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "theme", required = false) String theme,
            Model model) {
        List<NewsArticle> articles = newsService.getNews(keyword, theme);
        model.addAttribute("articles", articles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("theme", theme);
        model.addAttribute("displayTitle", resolveDisplayTitle(keyword, theme));
        return "index";
    }

    private String resolveDisplayTitle(String keyword, String theme) {
        if (StringUtils.hasText(keyword)) {
            return "検索結果: " + keyword.trim();
        }
        if (StringUtils.hasText(theme)) {
            return "テーマ: " + toDisplayTheme(theme);
        }
        return "AI の最新ニュース";
    }

    private String toDisplayTheme(String theme) {
        return switch (theme) {
            case "ai" -> "AI";
            case "generative-ai" -> "生成AI";
            case "nvidia" -> "NVIDIA";
            case "tsmc" -> "TSMC";
            case "it" -> "IT";
            case "java" -> "Java";
            case "semiconductor" -> "半導体";
            case "stock" -> "株式投資";
            case "japan-stock" -> "日本株";
            case "us-stock" -> "米国株";
            case "earnings" -> "決算";
            case "fx" -> "為替";
            case "interest-rate" -> "金利";
            default -> "AI";
        };
    }
}
