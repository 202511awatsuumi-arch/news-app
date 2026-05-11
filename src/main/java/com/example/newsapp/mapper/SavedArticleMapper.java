package com.example.newsapp.mapper;

import com.example.newsapp.model.SavedArticle;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SavedArticleMapper {
    @Select("""
            SELECT id, title, description, url, image_url, source_name, published_at, theme, saved_at
            FROM saved_articles
            ORDER BY saved_at DESC
            """)
    @Results(id = "savedArticleMap", value = {
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "sourceName", column = "source_name"),
            @Result(property = "publishedAt", column = "published_at"),
            @Result(property = "savedAt", column = "saved_at")
    })
    List<SavedArticle> findAll();

    @Select("SELECT COUNT(*) FROM saved_articles WHERE url = #{url}")
    int countByUrl(@Param("url") String url);

    @Insert("""
            INSERT INTO saved_articles (title, description, url, image_url, source_name, published_at, theme, saved_at)
            VALUES (#{title}, #{description}, #{url}, #{imageUrl}, #{sourceName}, #{publishedAt}, #{theme}, #{savedAt})
            """)
    void insert(SavedArticle article);

    @Delete("DELETE FROM saved_articles WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
}
