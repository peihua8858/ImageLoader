package com.fz.imageloader.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fz.imageloader.Constants;
import com.fz.imageloader.R;
import com.fz.imageloader.widget.RatioImageView;

/**
 * 旋转变换
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/1/3 08:44
 */
public class RotateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);
        RatioImageView imageView1 = findViewById(R.id.riv_image_1);
        imageView1.setImageUrl(Constants.urls[0]);
        RatioImageView imageView2 = findViewById(R.id.riv_image_2);
        imageView2.setImageUrl(Constants.urls[0]);
        RatioImageView imageView3 = findViewById(R.id.riv_image_3);
        imageView3.setImageUrl(Constants.urls[0]);
        RatioImageView imageView4 = findViewById(R.id.riv_image_4);
        imageView4.setImageUrl(Constants.urls[0]);
        RatioImageView imageView5 = findViewById(R.id.riv_image_5);
        imageView5.setImageUrl(Constants.urls[0]);
    }
}
