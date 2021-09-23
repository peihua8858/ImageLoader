package com.fz.imageloader

import android.content.Context

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