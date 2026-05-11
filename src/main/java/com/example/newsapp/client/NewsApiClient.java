package com.example.newsapp.client;

import com.example.newsapp.dto.CurrentsApiResponse;
import com.example.newsapp.dto.CurrentsNewsItem;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NewsApiClient {
    @Value("${news.api.key:}")
    private String apiKey;

    @Value("${news.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CurrentsNewsItem> searchNews(String keywords) {
        if (!StringUtils.hasText(apiKey)) {
            return Collections.emptyList();
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search")
                    .queryParam("keywords", keywords)
                    .queryParam("language", "ja")
                    .queryParam("apiKey", apiKey)
                    .toUriString();

            ResponseEntity<CurrentsApiResponse> response =
                    restTemplate.getForEntity(url, CurrentsApiResponse.class);
            CurrentsApiResponse body = response.getBody();

            if (body == null || body.getNews() == null) {
                return Collections.emptyList();
            }
            return body.getNews();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
