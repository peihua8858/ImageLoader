package com.fz.imageloader.demo

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fz.common.utils.getDiskCacheDir
import com.fz.common.utils.getFileFromUri
import com.fz.dialog.LoadingDialogFragment
import com.fz.imageloader.demo.activity.BlurActivity
import com.fz.imageloader.demo.activity.CropCircleActivity
import com.fz.imageloader.demo.activity.GifActivity
import com.fz.imageloader.demo.activity.GrayScaleActivity
import com.fz.imageloader.demo.activity.RotateActivity
import com.fz.imageloader.demo.glide.ImageCompress
import com.fz.imageloader.widget.RatioImageView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnOpen = findViewById<Button>(R.id.btn_open)
        btnOpen.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)
                .bindCustomChooseLimitListener { context, tips ->
                    Toast.makeText(context, tips, Toast.LENGTH_LONG).show()
                }
                .selectionMode(PictureConfig.SINGLE)
                .isPreviewImage(true)
                .isGif(true)
                .isWebp(true)
                .isCamera(true)
                .isZoomAnim(true)
                .isPreviewEggs(true)
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
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
        val imageView7 = findViewById<RatioImageView>(R.id.riv_image_8)
//        imageView7.setImageUrl(R.mipmap.ic_insure_loading)
//        ImageLoader.getInstance().createProcessor(ImageGlideFetcher())
//        ImageLoader.getInstance().loadImage(
//            ImageOptions.Builder()
//                .setImageUrl("https://uidesign.rglcdn.com/RG/image/others/20190830_12416/LOGO@3x.png")
//                .setTargetView(imageView7)
//                .setScaleType(GlideScaleType.CENTER_INSIDE)
//                .setLoaderListener(object : LoaderListener<Drawable> {
//                    override fun onSuccess(bitmap: Drawable?, width: Int, height: Int): Boolean {
//                        KLog.d("ImageLoader>>bitmap:[" + bitmap?.intrinsicWidth + ":" + bitmap?.intrinsicHeight + "]")
//                        return false
//                    }
//
//                    override fun onError(e: Exception?): Boolean {
//                        TODO("Not yet implemented")
//                    }
//                })
//                .build()
//        )
    }

    private var loadingDialogFragment: LoadingDialogFragment? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && data != null) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            val realFile: File? = this.getFileFromUri(selectList[0].path)
            if (realFile != null) {
                loadingDialogFragment = LoadingDialogFragment.createBuilder(this).show()
                AsyncTask.execute {
                    val inputStream = FileInputStream(realFile)
                    val byteArray = ByteArray(inputStream.available())
                    inputStream.read(byteArray)
                    var outputByteArray = ImageCompress.compressImageDataWithLongWidth(this, byteArray, 240)

                    if (outputByteArray != null) {
                        outputByteArray =
                            ImageCompress.compressImageDataWithSize(this, byteArray, 1250000)
                    }
                    val parentFile = getDiskCacheDir()
                    if (parentFile != null) {
                        val outputFile = createFile(parentFile, FILENAME, realFile.extension)
                        val outputStream = FileOutputStream(outputFile)
                        outputStream.write(outputByteArray)
                        outputStream.flush()
                        outputStream.close()
                    }
                    inputStream.close()
                    runOnUiThread {
                        loadingDialogFragment?.dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    /** Helper function used to create a timestamped file */
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, "IMG_" + SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + "." + extension
        )
}