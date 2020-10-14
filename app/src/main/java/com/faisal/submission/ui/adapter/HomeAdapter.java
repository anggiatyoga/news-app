package com.faisal.submission.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.faisal.submission.R;
import com.faisal.submission.model.Article;
import com.faisal.submission.model.ArticleEntities;
import com.faisal.submission.storage.NewsDatabase;
import com.faisal.submission.ui.activity.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Article> articleList;
    private Context context;
    private NewsDatabase database;

    public HomeAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
        database = Room.databaseBuilder(
                context.getApplicationContext(),
                NewsDatabase.class, "article"
        ).allowMainThreadQueries().build();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_adapter, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
        Article articleModel = articleList.get(position);

        ArticleEntities articleEntities = new ArticleEntities();
        articleEntities.setAuthor(articleModel.getAuthor());
        articleEntities.setTitle(articleModel.getTitle());
        articleEntities.setDescription(articleModel.getDescription());
        articleEntities.setUrl(articleModel.getUrl());
        articleEntities.setUrlToImage(articleModel.getUrlToImage());
        articleEntities.setPublishedAt(articleModel.getPublishedAt());
        articleEntities.setContent(articleModel.getContent());

        if (!TextUtils.isEmpty(articleModel.getTitle())) {
            holder.txtTitle.setText(articleModel.getTitle());
        }
        if (!TextUtils.isEmpty(articleModel.getDescription())) {
            holder.txtAuthor.setText(articleModel.getAuthor());
        }

        Glide.with(context)
                .load(articleModel.getUrlToImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_cloud_download).error(R.drawable.ic_baseline_broken_image).transform(new CenterCrop(), new RoundedCorners(10)))
                .into(holder.imgPhoto);

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

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_title)
        TextView txtTitle;
        @BindView(R.id.tv_item_author)
        TextView txtAuthor;
        @BindView(R.id.img_item_photo)
        ImageView imgPhoto;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
