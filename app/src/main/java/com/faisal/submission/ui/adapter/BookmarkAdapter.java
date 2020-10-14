package com.faisal.submission.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.faisal.submission.model.ArticleEntities;
import com.faisal.submission.storage.NewsDatabase;
import com.faisal.submission.ui.activity.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private List<ArticleEntities> articleList;
    private Context context;
    private NewsDatabase database;

    public BookmarkAdapter() {
    }

    public BookmarkAdapter(List<ArticleEntities> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
        database = Room.databaseBuilder(
                context.getApplicationContext(),
                NewsDatabase.class, "article"
        ).allowMainThreadQueries().build();
        notifyDataSetChanged();
    }

    public void removeItem(List<ArticleEntities> articleEntitiesList, int index) {
        Log.d("REMOVE_POSITION", String.valueOf(index));
        Log.d("REMOVE_TITLE", articleEntitiesList.get(index).getTitle());
        this.articleList = articleEntitiesList;
        articleList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, articleList.size());
    }


    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_adapter, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        ArticleEntities article = articleList.get(position);

        holder.txtTitle.setText(article.getTitle());
        holder.txtAuthor.setText(article.getAuthor());

        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).transform(new CenterCrop(), new RoundedCorners(10)))
                .into(holder.imgPhoto);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_NEWS, article);
            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_title)
        TextView txtTitle;
        @BindView(R.id.tv_item_author)
        TextView txtAuthor;
        @BindView(R.id.img_item_photo)
        ImageView imgPhoto;


        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




}
