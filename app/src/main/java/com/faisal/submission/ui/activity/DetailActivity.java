package com.faisal.submission.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.faisal.submission.R;
import com.faisal.submission.model.ArticleEntities;
import com.faisal.submission.storage.NewsDatabase;
import com.faisal.submission.ui.adapter.BookmarkAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_NEWS = "extra_news";
    public static final String EXTRA_POSITION = "extra_position";
    private NewsDatabase database;
    private BookmarkAdapter bookmarkAdapter;

    @BindView(R.id.tv_detail_title)
    TextView tvTitle;
    @BindView(R.id.tv_detail_published)
    TextView tvPublished;
    @BindView(R.id.tv_detail_content)
    TextView tvContent;
    @BindView(R.id.img_detail_banner)
    ImageView imgBanner;
    @BindView(R.id.btn_detail_back)
    ImageButton btnBack;
    @BindView(R.id.btn_detail_favorite)
    ImageButton btnFavorite;
    @BindView(R.id.btn_detail_share)
    ImageButton btnShare;

    ArticleEntities article;
    ArticleEntities article10;
    ArrayList<ArticleEntities> articleListDb;
    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        article10 = new ArticleEntities();
        articleListDb = new ArrayList<>();

        database = Room.databaseBuilder(
                getApplicationContext(),
                NewsDatabase.class,
                "article"
        ).allowMainThreadQueries().build();

        article = getIntent().getParcelableExtra(EXTRA_NEWS);

        ButterKnife.bind(this);

        btnBack.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        tvTitle.setText(article.getTitle());
        tvPublished.setText(article.getPublishedAt());
        tvContent.setText(article.getContent());

        Glide.with(this)
                .load(article.getUrlToImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_cloud_download).error(R.drawable.ic_baseline_broken_image).transform(new CenterCrop()))
                .into(imgBanner);


        article10 = checkBookmarkArticle(article.getTitle());
        articleListDb.addAll(database.newsDao().getArticle());


        if (article10 != null) {
            isFavorite = true;
            btnFavorite.setImageResource(R.drawable.ic_baseline_favorite);
        } else {
            isFavorite = false;
            btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void bookmarkArticle(final ArticleEntities article) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                ArticleEntities articleEntities = new ArticleEntities();
                articleEntities.setAuthor(article.getAuthor());
                articleEntities.setTitle(article.getTitle());
                articleEntities.setDescription(article.getDescription());
                articleEntities.setUrl(article.getUrl());
                articleEntities.setUrlToImage(article.getUrlToImage());
                articleEntities.setPublishedAt(article.getPublishedAt());
                articleEntities.setContent(article.getContent());
                articleEntities.setBookmarked(1);
                return database.newsDao().insertArticle(articleEntities);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Toast.makeText(DetailActivity.this, "Save Bookmark", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public void unBookmarkArticle(final ArticleEntities article) {
        bookmarkAdapter = new BookmarkAdapter();
        database.newsDao().deleteArticle(article);
        int index = getIndexOfList(article.getTitle());
        bookmarkAdapter.removeItem(articleListDb, index);
        Toast.makeText(this, "Delete Bookmark", Toast.LENGTH_SHORT).show();
    }

    private ArticleEntities checkBookmarkArticle(final String title) {
        ArticleEntities article1;
        article1 = database.newsDao().searchBookmarked(title);
        try {
            Log.d("CHECK_BOOKMARK", database.newsDao().searchBookmarked(title).getTitle());
        } catch (Exception e) {
            Log.d("CHECK_BOOKMARK", e.toString());
        }
        return article1;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_detail_back:
                DetailActivity.super.onBackPressed();
                break;
            case R.id.btn_detail_favorite:
                if (isFavorite) {
                    unBookmarkArticle(article);
                    btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border);
                    isFavorite = false;
                } else {
                    bookmarkArticle(article);
                    btnFavorite.setImageResource(R.drawable.ic_baseline_favorite);
                    isFavorite = true;
                }
                break;
            case R.id.btn_detail_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, article.getUrl());
                intent.setType("text/plain");

                Intent shareItem = Intent.createChooser(intent, null);
                startActivity(shareItem);
                break;

        }
    }

    private int getIndexOfList(String title) {
        for (int i = 0; i < articleListDb.size(); i++) {
            if (articleListDb.get(i).getTitle().equals(title)) {
                return i;
            }
        }
        return -1;
    }
}