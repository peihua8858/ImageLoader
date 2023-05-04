package com.fz.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.fz.imageloader.*
import com.fz.imageloader.glide.transformations.*
import com.fz.imageloader.widget.RatioImageView
import java.io.File

/**
 * glide图片加载
 * @author dingpeihua
 * @date 2021/9/22 14:22
 * @version 1.0
 */
class ImageGlideFetcher : IImageLoader {
    /**
     * 垂直反转
     */
    val VERTICAL_MATRIX = floatArrayOf(1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f)

    /**
     * 水平反转
     */
    val HORIZONTAL_MATRIX = floatArrayOf(-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
    override fun <T> loadImage(options: ImageOptions<T>) {
        var requestOptions = RequestOptions()
        if (options.isAutoCloneEnabled) {
            requestOptions.autoClone()
        }
        if (options.sizeMultiplier > 0) {
            requestOptions = requestOptions.sizeMultiplier(options.sizeMultiplier)
        }
        requestOptions = if (options.diskCacheStrategy != null) {
            requestOptions.diskCacheStrategy(getDiskCache(options.diskCacheStrategy))
        } else {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        }
        if (options.priority != null) {
            requestOptions = requestOptions.priority(getPriority(options.priority))
        }
        if (options.errorPlaceholder != null) {
            requestOptions = requestOptions.error(options.errorPlaceholder)
        } else if (options.errorId > 0) {
            requestOptions = requestOptions.error(options.errorId)
        }
        var hasPlaceholder = false
        if (options.placeholderDrawable != null) {
            hasPlaceholder = true
            requestOptions = requestOptions.placeholder(options.placeholderDrawable)
        } else if (options.placeholderId > 0) {
            hasPlaceholder = true
            requestOptions = requestOptions.placeholder(options.placeholderId)
        }
        if (options.overrideHeight > 0 || options.overrideWidth > 0) {
            requestOptions = requestOptions.override(
                if (options.overrideWidth > 0) options.overrideWidth else options.overrideHeight,
                if (options.overrideHeight > 0) options.overrideHeight else options.overrideWidth
            )
        } else if (!hasPlaceholder) {
            requestOptions = requestOptions.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        }

        requestOptions = requestOptions.skipMemoryCache(!options.isCacheAble)
        if (options.fallbackDrawable != null) {
            requestOptions = requestOptions.fallback(options.fallbackDrawable)
        }
        if (options.fallbackId > 0) {
            requestOptions = requestOptions.fallback(options.fallbackId)
        }
        if (options.theme != null) {
            requestOptions = requestOptions.theme(options.theme)
        }
        val multiTransformation = MultiTransformation<Bitmap>()
        if (options.matrixValues != null) {
            multiTransformation.addTransform(MatrixTransformation(options.matrixValues))
        } else if (options.reverseDirection > 0) {
            var values: FloatArray? = null
            when (options.reverseDirection) {
                RatioImageView.REVERSE_VERTICAL ->                     //垂直反转
                    values = VERTICAL_MATRIX
                RatioImageView.REVERSE_HORIZONTAL ->                     //水平反转，
                    values = HORIZONTAL_MATRIX
                RatioImageView.REVERSE_LOCALE ->                     //如果是RTL 则图片反向，否则保持不变
                    values = if (options.isRtl) HORIZONTAL_MATRIX else null
                else -> {
                }
            }
            if (values != null) {
                multiTransformation.addTransform(MatrixTransformation(values))
            }
        }
        if (options.isCropCircle) {
            multiTransformation.addTransform(CircleCrop())
        }
        if (options.roundedRadius > 0) {
            multiTransformation.addTransform(
                RoundedCornersTransformation(
                    options.roundedRadius,
                    options.roundedMargin,
                    options.cornerType
                )
            )
        }
        if (options.isGrayScale) {
            multiTransformation.addTransform(GrayScaleTransformation())
        }
        if (options.isBlur) {
            multiTransformation.addTransform(
                BlurTransformation(
                    options.fuzzyRadius,
                    options.sampling
                )
            )
        }
        if (options.rotateDegree > 0) {
            multiTransformation.addTransform(RotateTransformation(options.rotateDegree))
        }
        requestOptions = requestOptions.useAnimationPool(options.useAnimationPool)
        if (options.scaleType != null) {
            when (options.scaleType) {
                GlideScaleType.CENTER_INSIDE -> multiTransformation.addTransform(CenterInside())
                GlideScaleType.FIT_CENTER -> multiTransformation.addTransform(FitCenter())
                GlideScaleType.CENTER_CROP -> multiTransformation.addTransform(CenterCrop())
                GlideScaleType.CIRCLE_CROP -> multiTransformation.addTransform(CircleCrop())
                else -> {
                }
            }
        }
        requestOptions = requestOptions.optionalTransform(multiTransformation)
        requestOptions = requestOptions.optionalTransform(
            WebpDrawable::class.java,
            WebpDrawableTransformation(multiTransformation)
        )
        val imageView = options.targetView
        val requestManager: RequestManager = when {
            options.fragment != null -> {
                Glide.with(options.fragment!!)
            }
            options.activity != null -> {
                Glide.with(options.activity!!)
            }
            options.context != null -> {
                Glide.with(options.context!!)
            }
            options.target != null -> {
                Glide.with(options.target!!.imageView)
            }
            imageView != null -> {
                Glide.with(imageView)
            }
            else -> {
                throw IllegalArgumentException("Context is not available!")
            }
        }
        if (options.imageUrl == null || options.imageUrl.toString().isEmpty()) {
            Log.e("ImageLoad", "Url is null.")
            callError(
                imageView,
                options.errorPlaceholder,
                options.loaderListener, "Url is null."
            )
            return
        }
        when {
            options.isShowGif -> {
                loadImageUrl(
                    requestManager.asGif(),
                    requestOptions,
                    options as ImageOptions<GifDrawable>
                )
            }
            options.isShowBitmap -> {
                loadImageUrl(
                    requestManager.asBitmap(),
                    requestOptions,
                    options as ImageOptions<Bitmap>
                )
            }
            else -> {
                loadImageUrl(
                    requestManager.asDrawable(),
                    requestOptions,
                    options as ImageOptions<Drawable>
                )
            }
        }
    }

    private fun <T> loadImageUrl(
        requestBuilder: RequestBuilder<T>,
        requestOptions: RequestOptions,
        options: ImageOptions<T>
    ) {
        var builder = requestBuilder
        if (options.loaderListener != null) {
            builder = requestBuilder.listener(
                DRequestListener(
                    options.loaderListener as LoaderListener<T>,
                    options.overrideWidth,
                    options.overrideHeight
                )
            )
        }
        if (options.imageUrl is Int) {
            val resourceId = options.imageUrl as Int
            builder = builder.load(resourceId)
            builder = builder.apply(requestOptions)
        } else if (options.imageUrl is String) {
            var url = options.imageUrl as String
            url = url.trim { it <= ' ' }
            if (url.isEmpty()) {
                Log.e("ImageLoad", "Url is empty.")
                callError(
                    options.targetView,
                    options.errorPlaceholder,
                    options.loaderListener,
                    "Url is empty"
                )
                return
            }
            if (url.uppercase().startsWith("file://")) {
                builder = builder.load(File(url))
                builder = builder.apply(getFileOptions(requestOptions))
            } else {
                builder = builder.load(url)
                builder = builder.apply(requestOptions)
            }
        } else if (options.imageUrl is File) {
            builder = builder.load(options.imageUrl as File?)
            builder = builder.apply(getFileOptions(requestOptions))
        } else {
            builder = builder.load(options.imageUrl)
            builder = builder.apply(requestOptions)
        }
        val imageView = options.targetView
        val target = options.target
        when {
            target != null -> {
                builder.into(ImageTarget(target))
            }
            imageView is ImageView -> {
                builder.into(imageView)
            }
            else -> {
                builder.submit(
                    if (options.overrideWidth == 0) Target.SIZE_ORIGINAL else options.overrideWidth,
                    if (options.overrideHeight == 0) Target.SIZE_ORIGINAL else options.overrideHeight
                )
            }
        }
    }

    private fun getPriority(priority: com.fz.imageloader.Priority?): Priority {
        return when (priority) {
            com.fz.imageloader.Priority.HIGH -> Priority.HIGH
            com.fz.imageloader.Priority.IMMEDIATE -> Priority.IMMEDIATE
            com.fz.imageloader.Priority.LOW -> Priority.LOW
            else -> Priority.NORMAL
        }
    }

    private fun getDiskCache(diskCacheStrategy: com.fz.imageloader.DiskCacheStrategy?): DiskCacheStrategy {
        return when (diskCacheStrategy) {
            com.fz.imageloader.DiskCacheStrategy.ALL -> DiskCacheStrategy.ALL
            com.fz.imageloader.DiskCacheStrategy.DATA -> DiskCacheStrategy.DATA
            com.fz.imageloader.DiskCacheStrategy.RESOURCE -> DiskCacheStrategy.RESOURCE
            com.fz.imageloader.DiskCacheStrategy.NONE -> DiskCacheStrategy.NONE
            else -> DiskCacheStrategy.AUTOMATIC
        }
    }

    override fun clearMemoryCache(context: Context) {
        Glide.get(context).clearMemory()
    }

    override fun clearDiskCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    /**
     * 本地图片不用使用缓存，因为有的本地图片路径写死的不会变化，
     * 使用缓存就展示一直不变，而此时图片实际data可能变了
     *
     * @param options
     * @return
     */
    private fun getFileOptions(options: RequestOptions?): RequestOptions {
        return RequestOptions().apply(options!!)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
    }

    private fun callError(
        imageView: View?,
        errorPlaceholder: Drawable?,
        loaderListener: LoaderListener<*>?,
        msg: String?
    ) {
        if (imageView is ImageView) {
            imageView.setImageDrawable(errorPlaceholder)
        }
        loaderListener?.onError(Exception(msg))
    }

    /**
     * 默认监听器
     *
     * @author dingpeihua
     * @version 1.0
     * @date 2019/1/2 17:35
     */
    internal class DRequestListener<RESOURCE>(
        loaderListener: LoaderListener<RESOURCE>?,
        overrideWidth: Int,
        overrideHeight: Int
    ) :
        RequestListener<RESOURCE> {
        private val loaderListener: LoaderListener<RESOURCE>? = loaderListener
        private val overrideHeight: Int = overrideHeight
        private val overrideWidth: Int = overrideWidth
        override fun onLoadFailed(
            e: GlideException?,
            model: Any,
            target: Target<RESOURCE>,
            isFirstResource: Boolean
        ): Boolean {
            return loaderListener?.onError(e) ?: false
        }

        override fun onResourceReady(
            resource: RESOURCE,
            model: Any,
            target: Target<RESOURCE>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            if (loaderListener != null) {
                var width = overrideWidth
                var height = overrideHeight
                if (resource is Drawable) {
                    val drawable = resource as Drawable
                    width = drawable.intrinsicWidth
                    height = drawable.intrinsicHeight
                } else if (resource is Bitmap) {
                    val drawable = resource as Bitmap
                    width = drawable.width
                    height = drawable.height
                }
                return loaderListener.onSuccess(resource, width, height)
            }
            return false
        }

    }

    internal class ImageTarget<R>(
        private val target: com.fz.imageloader.ImageViewTarget<R>
    ) : ImageViewTarget<R>(target.imageView) {
        override fun onLoadStarted(placeholder: Drawable?) {
            super.onLoadStarted(placeholder)
            target.onLoadStarted(placeholder)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            super.onLoadFailed(errorDrawable)
            target.onLoadFailed(errorDrawable)
        }

        override fun setResource(resource: R?) {
            target.onResourceReady(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            super.onLoadCleared(placeholder)
            target.onLoadCleared(placeholder)
        }
    }
}
