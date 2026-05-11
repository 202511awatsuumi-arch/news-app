package com.example.newsapp.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSaveForm {
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String sourceName;
    private String publishedAt;
    private String theme;
}
