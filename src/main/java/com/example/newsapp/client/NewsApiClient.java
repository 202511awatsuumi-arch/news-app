package com.example.newsapp.client;

import com.example.newsapp.dto.CurrentsApiResponse;
import com.example.newsapp.dto.CurrentsNewsItem;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NewsApiClient {
    private static final Logger log = LoggerFactory.getLogger(NewsApiClient.class);
    private static final String LANGUAGE = "ja";

    @Value("${news.api.key:}")
    private String apiKey;

    @Value("${news.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CurrentsNewsItem> searchNews(String keywords) {
        log.info("Currents API search start. keywords='{}', language='{}'", keywords, LANGUAGE);

        if (!StringUtils.hasText(apiKey)) {
            log.info("Currents API search result. keywords='{}', language='{}', count=0 (skipped: api key not set)",
                    keywords,
                    LANGUAGE);
            return Collections.emptyList();
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search")
                    .queryParam("keywords", keywords)
                    .queryParam("language", LANGUAGE)
                    .queryParam("apiKey", apiKey)
                    .toUriString();

            ResponseEntity<CurrentsApiResponse> response =
                    restTemplate.getForEntity(url, CurrentsApiResponse.class);
            CurrentsApiResponse body = response.getBody();

            if (body == null || body.getNews() == null) {
                log.info("Currents API search result. keywords='{}', language='{}', count=0", keywords, LANGUAGE);
                return Collections.emptyList();
            }

            int count = body.getNews().size();
            log.info("Currents API search result. keywords='{}', language='{}', count={}", keywords, LANGUAGE, count);
            return body.getNews();
        } catch (Exception ex) {
            log.warn(
                    "Currents API search failed. keywords='{}', language='{}', errorType='{}', message='{}'",
                    keywords,
                    LANGUAGE,
                    ex.getClass().getSimpleName(),
                    ex.getMessage());
            return Collections.emptyList();
        }
    }
}