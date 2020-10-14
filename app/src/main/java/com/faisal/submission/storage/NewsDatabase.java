package com.faisal.submission.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.faisal.submission.model.ArticleEntities;

@Database(entities = {ArticleEntities.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();
}
