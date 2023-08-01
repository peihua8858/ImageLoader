package com.fz.imageloader

import android.content.Context
import android.view.View

interface IImageLoader {
    fun <T> loadImage(context: Context, url: Any, callback: (T?, Int, Int, Exception?) -> Boolean)
    fun <T> loadImage(context: Context, url: Any, callback: (T?, Exception?) -> Boolean)
    fun loadImage(targetView: View, url: Any)
    fun loadImage(targetView: View, url: Any, width: Int, height: Int)
    fun <T> loadImage(target: ImageViewTarget<T>, url: Any)
    fun <T> loadImage(target: ImageViewTarget<T>, url: Any, width: Int, height: Int)
    fun <T> loadImage(options: ImageOptions<T>)

    /**
     * 清理内存缓存
     */
    fun clearMemoryCache(context: Context)

    /**
     * 清理磁盘缓存
     */
    fun clearDiskCache(context: Context)
}