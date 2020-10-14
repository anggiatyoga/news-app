package com.faisal.submission.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.faisal.submission.R;
import com.faisal.submission.model.Article;
import com.faisal.submission.model.ArticleEntities;
import com.faisal.submission.ui.activity.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<Article> articleList;
    private LayoutInflater layoutInflater;
    private ViewPager2 viewPager2;


    public ViewPagerAdapter(List<Article> articleList, Context context, ViewPager2 viewPager2) {
        this.articleList = articleList;
        this.layoutInflater = LayoutInflater.from(context);
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_viewpager_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        Article article = articleList.get(position);

        ArticleEntities articleEntities = new ArticleEntities();
        articleEntities.setAuthor(article.getAuthor());
        articleEntities.setTitle(article.getTitle());
        articleEntities.setDescription(article.getDescription());
        articleEntities.setUrl(article.getUrl());
        articleEntities.setUrlToImage(article.getUrlToImage());
        articleEntities.setPublishedAt(article.getPublishedAt());
        articleEntities.setContent(article.getContent());

        holder.txtAuthor.setText(article.getAuthor());
        holder.txtPublished.setText(article.getPublishedAt());
        holder.txtTitle.setText(article.getTitle());

        Glide.with(layoutInflater.getContext())
                .load(article.getUrlToImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).transform(new CenterCrop(), new RoundedCorners(10)))
                .into(holder.imgBanner);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_NEWS, articleEntities);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_home_author)
        TextView txtAuthor;
        @BindView(R.id.tv_home_published)
        TextView txtPublished;
        @BindView(R.id.tv_home_title)
        TextView txtTitle;
        @BindView(R.id.img_home_banner)
        ImageView imgBanner;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }



}
