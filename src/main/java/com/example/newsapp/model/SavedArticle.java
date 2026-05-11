package com.example.newsapp.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedArticle {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String sourceName;
    private String publishedAt;
    private String theme;
    private LocalDateTime savedAt;
}
