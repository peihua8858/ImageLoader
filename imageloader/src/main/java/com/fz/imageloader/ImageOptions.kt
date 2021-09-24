package com.fz.imageloader

import android.app.Activity
import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.renderscript.RenderScript
import android.view.View
import androidx.fragment.app.Fragment

/**
 * 图片加载配置选项
 * @author dingpeihua
 * @date 2021/9/22 11:43
 * @version 1.0
 */
class ImageOptions<T>(
    /**
     * 当前上下文
     */
    val context: Context?,
    val fragment: Fragment?,
    val activity: Activity?,
    val targetView: View?,
    val sizeMultiplier: Float,
    val errorPlaceholder: Drawable?,
    val errorId: Int,
    val placeholderDrawable: Drawable?,
    val placeholderId: Int,
    val isCacheAble: Boolean,
    val overrideHeight: Int,
    val overrideWidth: Int,
    val fallbackDrawable: Drawable?,
    val fallbackId: Int,
    val theme: Theme?,
    val isAutoCloneEnabled: Boolean,
    val useUnlimitedSourceGeneratorsPool: Boolean,
    val onlyRetrieveFromCache: Boolean,
    val useAnimationPool: Boolean,
    val scaleType: GlideScaleType?,
    /**
     * 图片地址 包括网络地址、本地文件地址、资源id等
     */
    val imageUrl: Any?,
    /**
     * 圆角半径
     */
    val roundedRadius: Int,
    /**
     * 圆角
     */
    val roundedMargin: Int,
    /**
     * 是否是圆形
     */
    val isCropCircle: Boolean,
    /**
     * 灰度处理
     */
    val isGrayScale: Boolean,
    /**
     * 高斯模糊处理
     */
    val isBlur: Boolean,
    /**
     * 高斯模糊半径
     */
    val fuzzyRadius: Int,
    /**
     * 高斯模糊采样
     */
    val sampling: Int,
    /**
     * 旋转角度
     */
    val rotateDegree: Float,
    /**
     * 是否显示GIF
     */
    val isShowGif: Boolean,
    val isShowBitmap: Boolean,
    /**
     * 圆角类型
     */
    val cornerType: CornerType? = CornerType.ALL,
    val loaderListener: LoaderListener<T>? = null,
    val priority: Priority? = null,
    val diskCacheStrategy: DiskCacheStrategy? = null,
    val reverseDirection: Int = 0,
    val isRtl: Boolean = false,
    /**
     * 矩阵变化
     */
    val matrixValues: FloatArray? = null
) {

    companion object {
        /**
         * 带接收者的函数类型,这意味着我们需要向函数传递一个Builder类型的实例
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        /**
         * 当前上下文
         */
        private var context: Context? = null
        private var fragment: Fragment? = null
        private var activity: Activity? = null
        private var targetView: View? = null
        private var sizeMultiplier = 0f
        private var errorPlaceholder: Drawable? = null
        private var errorId = 0
        private var placeholderDrawable: Drawable? = null
        private var placeholderId = 0
        private var isCacheAble = true
        private var overrideHeight = 0
        private var overrideWidth = 0
        private var fallbackDrawable: Drawable? = null
        private var fallbackId = 0
        private var theme: Theme? = null
        private var isAutoCloneEnabled = false
        private var useUnlimitedSourceGeneratorsPool = false
        private var onlyRetrieveFromCache = false
        private var useAnimationPool = false
        private var scaleType: GlideScaleType? = null

        /**
         * 图片地址 包括网络地址、本地文件地址、资源id等
         */
        private var imageUrl: Any? = null

        /**
         * 圆角半径
         */
        private var roundedRadius = 0

        /**
         * 圆角
         */
        private var roundedMargin = 0

        /**
         * 是否是圆形
         */
        private var isCropCircle = false

        /**
         * 灰度处理
         */
        private var isGrayScale = false

        /**
         * 高斯模糊处理
         */
        private var isBlur = false

        /**
         * 高斯模糊半径
         */
        private var fuzzyRadius = 0

        /**
         * 高斯模糊采样
         */
        private var sampling = 0

        /**
         * 旋转角度
         */
        private var rotateDegree: Float = 0F

        /**
         * 是否显示GIF
         */
        private var isShowGif = false
        private var isShowBitmap: Boolean=false

        /**
         * 圆角类型
         */
        private var cornerType: CornerType? = CornerType.ALL
        private var loaderListener: LoaderListener<*>? = null
        private var priority: Priority? = null
        private var diskCacheStrategy: DiskCacheStrategy? = null
        private var reverseDirection: Int = 0
        private var isRtl: Boolean = false
        private var matrixValues: FloatArray? = null
        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setFragment(fragment: Fragment): Builder {
            this.fragment = fragment
            return this
        }

        fun setActivity(activity: Activity): Builder {
            this.activity = activity
            return this
        }

        fun setTargetView(targetView: View): Builder {
            this.targetView = targetView
            return this
        }

        fun setSizeMultiplier(sizeMultiplier: Float): Builder {
            this.sizeMultiplier = sizeMultiplier
            return this
        }

        fun setErrorPlaceholder(errorPlaceholder: Drawable?): Builder {
            this.errorPlaceholder = errorPlaceholder
            return this
        }

        fun setErrorId(errorId: Int): Builder {
            this.errorId = errorId
            return this
        }

        fun setPlaceholderDrawable(placeholderDrawable: Drawable?): Builder {
            this.placeholderDrawable = placeholderDrawable
            return this
        }

        fun setPlaceholderId(placeholderId: Int): Builder {
            this.placeholderId = placeholderId
            return this
        }

        fun setCacheAble(isCacheAble: Boolean): Builder {
            this.isCacheAble = isCacheAble
            return this
        }

        fun setOverrideHeight(overrideHeight: Int): Builder {
            this.overrideHeight = overrideHeight
            return this
        }

        fun setOverrideWidth(overrideWidth: Int): Builder {
            this.overrideWidth = overrideWidth
            return this
        }

        fun setFallbackDrawable(fallbackDrawable: Drawable): Builder {
            this.fallbackDrawable = fallbackDrawable
            return this
        }

        fun setFallbackId(fallbackId: Int): Builder {
            this.fallbackId = fallbackId
            return this
        }

        fun setTheme(theme: Theme): Builder {
            this.theme = theme
            return this
        }

        fun setAutoCloneEnabled(isAutoCloneEnabled: Boolean): Builder {
            this.isAutoCloneEnabled = isAutoCloneEnabled
            return this
        }

        fun setUseUnlimitedSourceGeneratorsPool(useUnlimitedSourceGeneratorsPool: Boolean): Builder {
            this.useUnlimitedSourceGeneratorsPool = useUnlimitedSourceGeneratorsPool
            return this
        }

        fun setOnlyRetrieveFromCache(onlyRetrieveFromCache: Boolean): Builder {
            this.onlyRetrieveFromCache = onlyRetrieveFromCache
            return this
        }

        fun setUseAnimationPool(useAnimationPool: Boolean): Builder {
            this.useAnimationPool = useAnimationPool
            return this
        }

        fun setScaleType(scaleType: GlideScaleType?): Builder {
            this.scaleType = scaleType
            return this
        }

        fun setImageUrl(imageUrl: Any): Builder {
            this.imageUrl = imageUrl
            return this
        }

        fun setRoundedRadius(roundedRadius: Int): Builder {
            this.roundedRadius = roundedRadius
            return this
        }

        fun setRoundedMargin(roundedMargin: Int): Builder {
            this.roundedMargin = roundedMargin
            return this
        }

        fun setCropCircle(isCropCircle: Boolean): Builder {
            this.isCropCircle = isCropCircle
            return this
        }

        fun setGrayScale(isGrayScale: Boolean): Builder {
            this.isGrayScale = isGrayScale
            return this
        }

        fun setBlur(isBlur: Boolean): Builder {
            this.isBlur = isBlur
            return this
        }

        fun setFuzzyRadius(fuzzyRadius: Int): Builder {
            this.fuzzyRadius = fuzzyRadius
            return this
        }

        fun setSampling(sampling: Int): Builder {
            this.sampling = sampling
            return this
        }

        fun setRotateDegree(rotateDegree: Float): Builder {
            this.rotateDegree = rotateDegree
            return this
        }

        fun setShowGif(isShowGif: Boolean): Builder {
            this.isShowGif = isShowGif
            return this
        }
        fun setBitmap(isShowBitmap: Boolean): Builder {
            this.isShowBitmap = isShowBitmap
            return this
        }
        fun setCornerType(cornerType: CornerType?): Builder {
            this.cornerType = cornerType
            return this
        }

        fun setLoaderListener(loaderListener: LoaderListener<*>?): Builder {
            this.loaderListener = loaderListener
            return this
        }

        fun setPriority(priority: Priority): Builder {
            this.priority = priority
            return this
        }

        fun setDiskCacheStrategy(diskCacheStrategy: DiskCacheStrategy?): Builder {
            this.diskCacheStrategy = diskCacheStrategy
            return this
        }

        fun setReverseDirection(reverseDirection: Int): Builder {
            this.reverseDirection = reverseDirection
            return this
        }

        fun setRtl(isRtl: Boolean): Builder {
            this.isRtl = isRtl
            return this
        }

        fun setMatrixValues(matrixValues: FloatArray?): Builder {
            this.matrixValues = matrixValues
            return this
        }

        fun build() = ImageOptions(
            context,
            fragment,
            activity,
            targetView,
            sizeMultiplier,
            errorPlaceholder,
            errorId,
            placeholderDrawable,
            placeholderId,
            isCacheAble,
            overrideHeight,
            overrideWidth,
            fallbackDrawable,
            fallbackId,
            theme,
            isAutoCloneEnabled,
            useUnlimitedSourceGeneratorsPool,
            onlyRetrieveFromCache,
            useAnimationPool,
            scaleType,
            imageUrl,
            roundedRadius,
            roundedMargin,
            isCropCircle,
            isGrayScale,
            isBlur,
            fuzzyRadius,
            sampling,
            rotateDegree,
            isShowGif,isShowBitmap,
            cornerType,
            loaderListener,
            priority,
            diskCacheStrategy,
            reverseDirection,
            isRtl,
            matrixValues
        )
    }
}