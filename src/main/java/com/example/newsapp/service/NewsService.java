package com.example.newsapp.service;

import com.example.newsapp.client.NewsApiClient;
import com.example.newsapp.dto.CurrentsNewsItem;
import com.example.newsapp.model.NewsArticle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewsService {
    private final NewsApiClient newsApiClient;

    public NewsService(NewsApiClient newsApiClient) {
        this.newsApiClient = newsApiClient;
    }

    public List<NewsArticle> getNews(String keyword, String theme) {
        String searchKeyword = resolveSearchKeyword(keyword, theme);
        List<CurrentsNewsItem> items = newsApiClient.searchNews(searchKeyword);

        if (items.isEmpty()) {
            return createDummyNews();
        }

        List<NewsArticle> result = new ArrayList<>();
        for (CurrentsNewsItem item : items) {
            result.add(mapToArticle(item, searchKeyword));
        }
        return result.isEmpty() ? createDummyNews() : result;
    }

    private String resolveSearchKeyword(String keyword, String theme) {
        if (StringUtils.hasText(keyword)) {
            return keyword.trim();
        }
        if (StringUtils.hasText(theme)) {
            return toThemeKeyword(theme);
        }
        return "AI";
    }

    private String toThemeKeyword(String theme) {
        return switch (theme) {
            case "ai" -> "AI";
            case "generative-ai" -> "生成AI";
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
