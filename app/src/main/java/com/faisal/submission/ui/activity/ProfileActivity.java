package com.faisal.submission.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.faisal.submission.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.btn_profile_back)
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        btnBack.setOnClickListener(view -> ProfileActivity.super.onBackPressed());

        Glide.with(this)
                .load(R.drawable.profile_image)
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(20)))
                .into(imgProfile);

    }
}