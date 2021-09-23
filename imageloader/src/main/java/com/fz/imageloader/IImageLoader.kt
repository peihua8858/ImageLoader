package com.fz.imageloader

import android.content.Context

interface IImageLoader {
    fun <T>loadImage(options: ImageOptions<T>)

    /**
     * 清理内存缓存
     */
    fun clearMemoryCache(context: Context)

    /**
     * 清理磁盘缓存
     */
    fun clearDiskCache(context: Context)
}