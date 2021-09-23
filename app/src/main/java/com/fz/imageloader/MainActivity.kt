package com.fz.imageloader

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.fz.imageloader.activity.*
import com.fz.imageloader.glide.ImageGlideFetcher
import com.socks.library.KLog
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)
        val btnCircle = findViewById<Button>(R.id.btn_circle)
        btnCircle.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    CropCircleActivity::class.java
                )
            )
        }
        val btnRotate = findViewById<Button>(R.id.btn_rotate)
        btnRotate.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    RotateActivity::class.java
                )
            )
        }
        val btnBlur = findViewById<Button>(R.id.btn_blur)
        btnBlur.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    BlurActivity::class.java
                )
            )
        }
        val btnGrayScale = findViewById<Button>(R.id.btn_gray_scale)
        btnGrayScale.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    GrayScaleActivity::class.java
                )
            )
        }
        val btnGif = findViewById<Button>(R.id.btn_gif)
        btnGif.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    GifActivity::class.java
                )
            )
        }
        val imageView7 = findViewById<ImageView>(R.id.riv_image_7)
        ImageLoader.getInstance().createProcessor(ImageGlideFetcher())
        ImageLoader.getInstance().loadImage(
            ImageOptions.Builder()
                .setImageUrl("https://uidesign.rglcdn.com/RG/image/others/20190830_12416/LOGO@3x.png")
                .setTargetView(imageView7)
                .setScaleType(GlideScaleType.CENTER_INSIDE)
                .setLoaderListener(object : LoaderListener<Drawable?> {
                    override fun onSuccess(bitmap: Drawable?, width: Int, height: Int): Boolean {
                        KLog.d("ImageLoader>>bitmap:[" + bitmap?.intrinsicWidth + ":" + bitmap?.intrinsicHeight + "]")
                        return false
                    }

                    override fun onError(e: Exception): Boolean {
                        return false
                    }
                })
                .build()
        )
    }
}