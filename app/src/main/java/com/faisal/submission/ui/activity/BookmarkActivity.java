package com.faisal.submission.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ImageButton;

import com.faisal.submission.R;
import com.faisal.submission.model.ArticleEntities;
import com.faisal.submission.storage.NewsDatabase;
import com.faisal.submission.ui.adapter.BookmarkAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookmarkAdapter adapter;
    private NewsDatabase database;
    private List<ArticleEntities> articleList;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        btnBack = findViewById(R.id.ic_bookmark_back);
        btnBack.setOnClickListener(view -> BookmarkActivity.super.onBackPressed());

        articleList = new ArrayList<>();

        database = Room.databaseBuilder(
                getApplicationContext(),
                NewsDatabase.class,
                "article"
        ).allowMainThreadQueries().build();

        articleList.addAll(database.newsDao().getArticle());

        recyclerView = findViewById(R.id.bookmark_rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookmarkActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookmarkAdapter(articleList, BookmarkActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }


}