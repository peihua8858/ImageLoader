package com.fz.imageloader.demo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fz.imageloader.GlideScaleType;
import com.fz.imageloader.ImageLoader;
import com.fz.imageloader.ImageOptions;
import com.fz.imageloader.LoaderListener;
import com.fz.imageloader.demo.R;
import com.socks.library.KLog;

/**
 * 主界面入口
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/1/3 08:55
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCircle = findViewById(R.id.btn_circle);
        btnCircle.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CropCircleActivity.class)));
        Button btnRotate = findViewById(R.id.btn_rotate);
        btnRotate.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RotateActivity.class)));
        Button btnBlur = findViewById(R.id.btn_blur);
        btnBlur.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BlurActivity.class)));
        Button btnGrayScale = findViewById(R.id.btn_gray_scale);
        btnGrayScale.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GrayScaleActivity.class)));
        Button btnGif = findViewById(R.id.btn_gif);
        btnGif.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GifActivity.class)));
        ImageView imageView7 = findViewById(R.id.riv_image_7);
        ImageLoader.getInstance().loadImage(new ImageOptions.Builder()
                .setImageUrl("https://uidesign.rglcdn.com/RG/image/others/20190830_12416/LOGO@3x.png")
                .setTargetView(imageView7)
                .setScaleType(GlideScaleType.CENTER_INSIDE)
                .setLoaderListener(new LoaderListener<Drawable>() {
                    @Override
                    public boolean onSuccess(Drawable bitmap, int width, int height) {
                        KLog.d("ImageLoader>>bitmap:[" + bitmap.getIntrinsicWidth() + ":" + bitmap.getIntrinsicHeight() + "]");
                        return false;
                    }

                    @Override
                    public boolean onError(Exception e) {
                        return false;
                    }
                })
                .build());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_circle:
                startActivity(new Intent(this, CropCircleActivity.class));
                break;
            case R.id.btn_rotate:
                startActivity(new Intent(this, RotateActivity.class));
                break;
            case R.id.btn_blur:
                startActivity(new Intent(this, BlurActivity.class));
                break;
            case R.id.btn_gray_scale:
                startActivity(new Intent(this, GrayScaleActivity.class));
                break;
            case R.id.btn_gif:
                startActivity(new Intent(this, GifActivity.class));
                break;
            default:
                break;
        }
    }
}
