package com.fz.imageloader.picasso

import android.content.Context
import com.fz.imageloader.IImageLoader
import com.fz.imageloader.ImageOptions
import java.lang.NullPointerException
import android.graphics.drawable.Drawable

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.fz.imageloader.GlideScaleType
import com.fz.imageloader.LoaderListener
import com.squareup.picasso.*
import com.squareup.picasso.Picasso.LoadedFrom
import java.io.File
import java.lang.Exception


open class ImagePicassoFetcher : IImageLoader {
    @Volatile
    private var sPicassoSingleton: Picasso? = null
    private val PICASSO_CACHE = "picasso-cache"
    private fun getPicasso(context: Context): Picasso? {
        if (sPicassoSingleton == null) {
            synchronized(ImagePicassoFetcher::class.java) {
                if (sPicassoSingleton == null) {
                    sPicassoSingleton = Picasso.Builder(context.applicationContext).build()
                }
            }
        }
        return sPicassoSingleton
    }

    override fun <T> loadImage(options: ImageOptions<T>) {
        var requestCreator: RequestCreator? = null
        var context: Context? = null
        when {
            options.activity != null -> {
                context = options.activity!!
            }
            options.context != null -> context = options.context
            options.fragment != null -> context = options.fragment?.context
            options.targetView != null -> context = options.targetView?.context
        }
        if (context == null) {
            throw NullPointerException("context must not be null")
        }
        val imageUrl = options.imageUrl
        when {
            imageUrl is String -> {
                requestCreator = getPicasso(context)?.load(imageUrl)
            }
            imageUrl is File -> {
                requestCreator = getPicasso(context)?.load(imageUrl)
            }
            imageUrl is Int && imageUrl != 0 -> {
                requestCreator = getPicasso(context)?.load(imageUrl)
            }
            imageUrl is Uri -> {
                requestCreator = getPicasso(context)?.load(imageUrl)
            }
        }
//        if (requestCreator == null) {
//            throw NullPointerException("requestCreator must not be null")
//        }
//        if (options.overrideHeight > 0 && options.overrideWidth > 0) {
//            requestCreator.resize(options.overrideWidth, options.overrideHeight)
//        }
//        if (options.scaleType != null) {
//            when (options.scaleType) {
//                GlideScaleType.CENTER_INSIDE -> requestCreator.centerInside()
//                GlideScaleType.FIT_CENTER -> multiTransformation.addTransform(FitCenter())
//                GlideScaleType.CENTER_CROP -> requestCreator.centerCrop()
//                GlideScaleType.CIRCLE_CROP -> multiTransformation.addTransform(CircleCrop())
//                else -> {
//                }
//            }
//        }
//
//        if (options.config != null) {
//            requestCreator.config(options.config)
//        }
//        if (options.errorResId !== 0) {
//            requestCreator.error(options.errorResId)
//        }
//        if (options.placeholderResId !== 0) {
//            requestCreator.placeholder(options.placeholderResId)
//        }
//        if (options.bitmapAngle !== 0) {
//            requestCreator.transform(PicassoTransformation(options.bitmapAngle))
//        }
//        if (options.skipLocalCache) {
//            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//        }
//        if (options.skipNetCache) {
//            requestCreator.networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
//        }
//        if (options.degrees !== 0) {
//            requestCreator.rotate(options.degrees)
//        }
//
//        if (options.targetView is ImageView) {
//            requestCreator.into(options.targetView as? ImageView)
//        } else if (options.loaderListener != null) {
//            requestCreator.into(
//                PicassoTarget(
//                    options.loaderListener as LoaderListener<Bitmap>,
//                    options.overrideWidth,
//                    options.overrideHeight
//                )
//            )
//        }
    }

    override fun clearMemoryCache(context: Context) {

    }

    override fun clearDiskCache(context: Context) {
        val diskFile = File(context.cacheDir, PICASSO_CACHE)
        if (diskFile.exists()) {
            //这边自行写删除代码
            diskFile.delete()
        }
    }

    internal class PicassoTarget constructor(
        callBack: LoaderListener<Bitmap>?, overrideWidth: Int,
        overrideHeight: Int
    ) :
        com.squareup.picasso.Target {
        private var callBack: LoaderListener<Bitmap>? = callBack
        private val overrideHeight: Int = overrideHeight
        private val overrideWidth: Int = overrideWidth
        override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
            callBack?.onSuccess(
                bitmap,
                bitmap?.width ?: overrideWidth,
                bitmap?.height ?: overrideHeight
            )
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            callBack?.onError(e)
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

    }
}