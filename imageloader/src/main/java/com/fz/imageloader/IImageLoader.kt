package com.fz.imageloader

import android.content.Context
import android.view.View
import androidx.annotation.Keep

interface IImageLoader {
    companion object {
        /**
         * 加载url 并返回[android.graphics.Bitmap]
         */
        const val TYPE_BITMAP: Int = 1

        /**
         * 加载url并返回gif
         */
        const val TYPE_GIF: Int = 2

        /**
         * 加载url并返回[android.graphics.drawable.Drawable]
         */
        const val TYPE_DRAWABLE = 3
    }

    /**
     * 加载[url]并通过[callback] 回调[type]对应的类型的结果
     * @param context 当前上下文
     * @param url 需要加载的url
     * @param type 加载类型，@see [TYPE_BITMAP]、[TYPE_GIF]、[TYPE_DRAWABLE]
     * @param callback 加载成功或失败后的回调
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */

    fun <T> loadImage(
        context: Context,
        url: Any, type: Int = TYPE_BITMAP,
        callback: ((T?, Int, Int, Exception?) -> Boolean)? = null
    )

    /**
     * 加载[url]并将[type]对应的类型的结果设置给[targetView]视图
     * @param targetView 目标视图
     * @param url 需要加载的url
     * @param type 加载类型，@see [TYPE_BITMAP]、[TYPE_GIF]、[TYPE_DRAWABLE]
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */
    fun loadImage(targetView: View, url: Any, type: Int = TYPE_BITMAP)

    /**
     * 加载[url]并将[type]对应的类型的结果设置给[targetView]视图
     * @param targetView 目标视图
     * @param url 需要加载的url
     * @param type 加载类型，@see [TYPE_BITMAP]、[TYPE_GIF]、[TYPE_DRAWABLE]
     * @param options 对应第三方加载类库的配置项，如：Glide中的RequestOptions
     * @param signature  对应第三方加载类库的签名，如：Glide中的Key
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */
    fun loadImage(
        targetView: View,
        url: Any,
        type: Int = TYPE_BITMAP,
        options: Any,
        signature: Any? = null
    )

    /**
     * 加载[url]并将[type]对应的类型的结果设置给[targetView]视图
     * @param targetView 目标视图
     * @param url 需要加载的url
     * @param type 加载类型，@see [TYPE_BITMAP]、[TYPE_GIF]、[TYPE_DRAWABLE]
     * @param width 加载图片的宽度
     * @param height  加载图片的高度
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */
    fun loadImage(targetView: View, url: Any, width: Int, height: Int, type: Int = TYPE_BITMAP)

    /**
     * 加载[url]并将[type]对应的类型的结果返回给[target]
     * @param target 目标回调[ImageViewTarget]
     * @param url 需要加载的url
     * @param type 加载类型，@see [TYPE_BITMAP]、[TYPE_GIF]、[TYPE_DRAWABLE]
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */
    fun <T> loadImage(target: ImageViewTarget<T>, url: Any, type: Int = TYPE_BITMAP)

    /**
     * 加载[url]并将[type]对应的类型的结果返回给[target]
     * @param target 目标回调[ImageViewTarget]
     * @param url 需要加载的url
     * @param type 加载类型，@see [TYPE_BITMAP]、[TYPE_GIF]、[TYPE_DRAWABLE]
     * @param width 加载图片的宽度
     * @param height  加载图片的高度
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */
    fun <T> loadImage(
        target: ImageViewTarget<T>,
        url: Any,
        width: Int,
        height: Int,
        type: Int = TYPE_BITMAP
    )

    /**
     * 根据配置项加载图片地址
     * @param options 图片加载配置项
     * @author dingpeihua
     * @date 2023/8/1 9:39
     * @version 1.0
     */

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