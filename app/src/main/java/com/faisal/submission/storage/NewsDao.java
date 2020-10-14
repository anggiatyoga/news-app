package com.faisal.submission.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faisal.submission.model.ArticleEntities;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM article")
    List<ArticleEntities> getArticle();

//    @Query("SELECT * FROM article where bookmarked = 1")
//    List<Article> getArticleBookmarked();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertArticle(ArticleEntities article);

    @Delete
    void deleteArticle(ArticleEntities article);

    @Query("SELECT * FROM article WHERE title = :title")
    ArticleEntities searchBookmarked(String title);


}
