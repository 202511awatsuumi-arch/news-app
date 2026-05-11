package com.example.newsapp.service;

import com.example.newsapp.form.ArticleSaveForm;
import com.example.newsapp.mapper.SavedArticleMapper;
import com.example.newsapp.model.SavedArticle;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SavedArticleService {
    private final SavedArticleMapper savedArticleMapper;

    public SavedArticleService(SavedArticleMapper savedArticleMapper) {
        this.savedArticleMapper = savedArticleMapper;
    }

    public void save(ArticleSaveForm form) {
        if (form == null) {
            return;
        }

        String url = normalize(form.getUrl());
        if (!StringUtils.hasText(url)) {
            return;
        }

        if (savedArticleMapper.countByUrl(url) > 0) {
            return;
        }

        SavedArticle article = new SavedArticle();
        article.setTitle(StringUtils.hasText(form.getTitle()) ? form.getTitle().trim() : "タイトルなし");
        article.setDescription(form.getDescription());
        article.setUrl(url);
        article.setImageUrl(form.getImageUrl());
        article.setSourceName(form.getSourceName());
        article.setPublishedAt(form.getPublishedAt());
        article.setTheme(form.getTheme());
        article.setSavedAt(LocalDateTime.now());

        savedArticleMapper.insert(article);
    }

    public List<SavedArticle> findAll() {
        return savedArticleMapper.findAll();
    }

    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        savedArticleMapper.deleteById(id);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
