package com.fz.imageloader

/**
 * 加载图片回调
 *
 * @author longxl
 * @version 1.0
 * @email 343827585@qq.com
 * @date 2016/7/15
 * @since 1.0
 */
interface LoaderListener<R> {
    /**
     * 开始加载图片
     * @param options 加载参数选项
     * @author dingpeihua
     * @date 2023/5/16 16:09
     * @version 1.0
     */
    fun onLoadStarted(options: ImageOptions<R>) {

    }

    /**
     * 图片加载成功
     *
     * @param bitmap
     * @param width
     * @param height
     * @return 阻止调用 [Target.onResourceReady]
     *      `target`，通常是因为侦听器想要更新{@code目标}或对象
     *      `target`包装自己或`false`允许[Target.onResourceReady]
     *     `target`上调用。
     * @author dingpeihua
     * @date 2019/1/2 17:59
     * @version 1.0
     */
    fun onSuccess(bitmap: R?, width: Int, height: Int): Boolean

    /**
     * 图片加载失败
     *
     * @param e
     * @return 阻止调用 [Target.onLoadFailed]
     *      `target`，通常是因为侦听器想要更新{@code目标}或对象
     *      `target`包装自己或`false`允许[Target.onLoadFailed]
     *     `target`上调用。
     * @author dingpeihua
     * @date 2019/1/2 17:59
     * @version 1.0
     */
    fun onError(e: Exception?): Boolean
}