package com.fz.imageloader

import android.content.Context
import android.view.View

/**
 * 图片加载工具
 * @author dingpeihua
 * @date 2021/9/22 19:20
 * @version 1.0
 */
class ImageLoader : IImageLoader {
    private var mImageFetcher: IImageLoader? = null

    private fun ImageFetcherHelper() {
    }

    private object Internal {
        var httpHelper: ImageLoader = ImageLoader()
    }

    companion object {
        @JvmStatic
        fun getInstance(): ImageLoader {
            return Internal.httpHelper
        }
    }


    fun createProcessor(imageFetcher: IImageLoader) {
        getInstance().mImageFetcher = imageFetcher
    }

    override fun <T> loadImage(
        context: Context,
        url: Any,
        type: Int,
        callback: ((T?, Int, Int, Exception?) -> Boolean)?
    ) {
        mImageFetcher?.loadImage(context, url, type, callback)
    }

    override fun loadImage(targetView: View, url: Any, type: Int) {
        mImageFetcher?.loadImage(targetView, url, type)
    }

    override fun loadImage(targetView: View, url: Any, type: Int, options: Any, signature: Any?) {
        mImageFetcher?.loadImage(targetView, url, type, options, signature)
    }

    override fun loadImage(targetView: View, url: Any, width: Int, height: Int, type: Int) {
        mImageFetcher?.loadImage(targetView, url, width, height, type)
    }

    override fun <T> loadImage(target: ImageViewTarget<T>, url: Any, type: Int) {
        mImageFetcher?.loadImage(target, url, type)
    }

    override fun <T> loadImage(
        target: ImageViewTarget<T>,
        url: Any,
        width: Int,
        height: Int,
        type: Int
    ) {
        mImageFetcher?.loadImage(target, url,width, height, type)
    }

    override fun <T> loadImage(options: ImageOptions<T>) {
        mImageFetcher?.loadImage(options)
    }

    override fun clearMemoryCache(context: Context) {
        mImageFetcher?.clearMemoryCache(context)
    }

    override fun clearDiskCache(context: Context) {
        mImageFetcher?.clearDiskCache(context)
    }
}