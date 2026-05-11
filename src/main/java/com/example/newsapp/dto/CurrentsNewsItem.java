package com.example.newsapp.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentsNewsItem {
    private String id;
    private String title;
    private String description;
    private String url;
    private String author;
    private String image;
    private String language;
    private List<String> category;
    private String published;
}
