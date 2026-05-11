package com.example.newsapp.service;

import com.example.newsapp.client.NewsApiClient;
import com.example.newsapp.dto.CurrentsNewsItem;
import com.example.newsapp.model.NewsArticle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewsService {
    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    private final NewsApiClient newsApiClient;

    public NewsService(NewsApiClient newsApiClient) {
        this.newsApiClient = newsApiClient;
    }

    public List<NewsArticle> getNews(String keyword, String theme) {
        String displayTheme = resolveDisplayTheme(keyword, theme);
        List<String> candidates = resolveSearchKeywords(keyword, theme);

        for (String candidate : candidates) {
            log.info("Trying news search keyword: {}", candidate);
            List<CurrentsNewsItem> items = newsApiClient.searchNews(candidate);
            if (!items.isEmpty()) {
                log.info("News search succeeded. keyword={}, count={}", candidate, items.size());
                return mapToArticles(items, displayTheme);
            }
        }

        log.info("No news found for all keywords. fallback dummy news.");
        return createDummyNews();
    }

    private List<NewsArticle> mapToArticles(List<CurrentsNewsItem> items, String displayTheme) {
        List<NewsArticle> result = new ArrayList<>();
        for (CurrentsNewsItem item : items) {
            result.add(mapToArticle(item, displayTheme));
        }
        return result;
    }

    private String resolveDisplayTheme(String keyword, String theme) {
        if (StringUtils.hasText(keyword)) {
            return keyword.trim();
        }
        if (StringUtils.hasText(theme)) {
            return toThemeKeyword(theme);
        }
        return "AI";
    }

    private List<String> resolveSearchKeywords(String keyword, String theme) {
        if (StringUtils.hasText(keyword)) {
            return toKeywordCandidates(keyword.trim());
        }
        if (StringUtils.hasText(theme)) {
            return toThemeCandidates(theme);
        }
        return List.of("AI");
    }

    private List<String> toThemeCandidates(String theme) {
        return switch (theme) {
            case "ai" -> List.of("AI", "生成AI", "ChatGPT");
            case "generative-ai" -> List.of("生成AI", "ChatGPT", "AI");
            case "nvidia" -> List.of("NVIDIA", "AI");
            case "tsmc" -> List.of("TSMC", "NVIDIA", "AI");
            default -> List.of("AI");
        };
    }

    private List<String> toKeywordCandidates(String keyword) {
        List<String> candidates = switch (keyword) {
            case "半導体" -> List.of("半導体", "NVIDIA", "TSMC", "AI");
            default -> List.of(keyword);
        };

        Set<String> unique = new LinkedHashSet<>(candidates);
        return new ArrayList<>(unique);
    }

    private String toThemeKeyword(String theme) {
        return switch (theme) {
            case "ai" -> "AI";
            case "generative-ai" -> "生成AI";
            case "nvidia" -> "NVIDIA";
            case "tsmc" -> "TSMC";
            default -> "AI";
        };
    }

    private NewsArticle mapToArticle(CurrentsNewsItem item, String displayTheme) {
        String sourceName = StringUtils.hasText(item.getAuthor()) ? item.getAuthor() : "ニュース提供元";
        return new NewsArticle(
                item.getTitle(),
                item.getDescription(),
                item.getUrl(),
                item.getImage(),
                sourceName,
                item.getPublished(),
                displayTheme);
    }

    private List<NewsArticle> createDummyNews() {
        List<NewsArticle> articles = new ArrayList<>();
        articles.add(new NewsArticle(
                "生成AIツールの企業導入が加速、業務効率化の実例が増加",
                "国内企業で生成AIの本格導入が進み、レポート作成や顧客対応の自動化など具体的な成果が報告されています。",
                "",
                "",
                "Tech Daily",
                LocalDate.now().toString(),
                "AI"));
        articles.add(new NewsArticle(
                "決算発表シーズン本格化、注目セクターに資金が集中",
                "主要企業の決算発表を受けて、半導体やクラウド関連銘柄に買いが入り、相場の物色動向が鮮明になっています。",
                "",
                "",
                "Market Watcher",
                LocalDate.now().minusDays(1).toString(),
                "株式投資"));
        articles.add(new NewsArticle(
                "米金融政策の見通しで為替が変動、ドル円は方向感を探る展開",
                "金利見通しに関する発言を受けて為替市場の値動きが大きくなり、投資家は次回会合の内容に注目しています。",
                "",
                "",
                "Economic Times JP",
                LocalDate.now().minusDays(2).toString(),
                "為替"));
        return articles;
    }
}
