package com.fz.imageloader.picasso

import android.content.Context
import com.fz.imageloader.IImageLoader
import com.fz.imageloader.ImageOptions

open class ImagePicassoFetcher : IImageLoader {
    override fun <T> loadImage(options: ImageOptions<T>) {

    }

    override fun clearMemoryCache(context: Context) {

    }

    override fun clearDiskCache(context: Context) {

    }
}