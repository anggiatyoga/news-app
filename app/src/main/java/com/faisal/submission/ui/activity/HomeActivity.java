package com.faisal.submission.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.faisal.submission.R;
import com.faisal.submission.connection.ApiClient;
import com.faisal.submission.connection.ApiService;
import com.faisal.submission.model.Article;
import com.faisal.submission.model.News;
import com.faisal.submission.ui.adapter.ViewPagerAdapter;
import com.faisal.submission.ui.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.ic_profile)
    ImageButton icProfile;
    @BindView(R.id.ic_bookmark)
    ImageButton icBookmark;
    @BindView(R.id.swipe_refresh_home)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.vp_home)
    ViewPager2 viewPager;

    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        ButterKnife.bind(this);

        fetchData();

        swipeRefreshLayout.setOnRefreshListener(() -> fetchData());

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        icProfile.setOnClickListener(this);
        icBookmark.setOnClickListener(this);
    }

    private void fetchData() {
        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<News> call = service.getTopHeadlines();
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                generateDataList(response.body().getArticles());
                setViewPager(response.body().getArticles());
                Log.d("RESPONSE_BODY", response.body().getArticles().get(0).getTitle());
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(HomeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<Article> articleList) {
        recyclerView = findViewById(R.id.home_rv);
        homeAdapter = new HomeAdapter(articleList , this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeAdapter);
    }



    private void setViewPager(List<Article> articleList) {
        List<Article> articles = new ArrayList<>();
        articles.add(articleList.get(0));
        articles.add(articleList.get(1));
        articles.add(articleList.get(2));
        articles.add(articleList.get(3));
        articles.add(articleList.get(4));
        articles.add(articleList.get(5));
        viewPager.setAdapter(new ViewPagerAdapter(articles, this, viewPager));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_profile:
                Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.ic_bookmark:
                Intent intentBookmark = new Intent(HomeActivity.this, BookmarkActivity.class);
                startActivity(intentBookmark);
                break;

        }
    }

}