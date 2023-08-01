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
        callback: ((T?, Int, Int, Exception?) -> Boolean)?
    ) {
        mImageFetcher?.loadImage(context, url, callback)
    }

    override fun loadImage(targetView: View, url: Any) {
        mImageFetcher?.loadImage(targetView, url)
    }

    override fun loadImage(targetView: View, url: Any, options: Any, signature: Any?) {
        mImageFetcher?.loadImage(targetView, url, options, signature)
    }

    override fun loadImage(targetView: View, url: Any, width: Int, height: Int) {
        mImageFetcher?.loadImage(targetView, url, width, height)
    }

    override fun <T> loadImage(target: ImageViewTarget<T>, url: Any) {
        mImageFetcher?.loadImage(target, url)
    }

    override fun <T> loadImage(target: ImageViewTarget<T>, url: Any, width: Int, height: Int) {
        mImageFetcher?.loadImage(target, url, width, height)
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