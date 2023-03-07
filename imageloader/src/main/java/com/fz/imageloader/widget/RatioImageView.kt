package com.fz.imageloader.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.fz.imageloader.*
import com.fz.imageloader.utils.Utils
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * 封装glide图片加载处理及变换，但由于glide限制不支持多种变换组合。
 * [setImageUrl]加载图片，glide 默认缩放
 * [setImageUrl]宽和高按照给定比例缩放显示
 * [setImageUrl]加载图片并重设宽度和高度
 * [setImageUrl]按照给定的请求选项加载图片
 * [setImageUrl]按照给定的最大宽度等比缩放图片
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/1/2 09:50
 */
open class RatioImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    @IntDef(REVERSE_VERTICAL, REVERSE_HORIZONTAL, REVERSE_LOCALE)
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class Direction

    /**
     * [ImageOptions.placeholderDrawable]
     */
    var placeholderDrawable: Drawable? = null

    /**
     * [ImageOptions.errorPlaceholder]
     */
    var errorDrawable: Drawable? = null

    /**
     * 圆角半径
     */
    private var roundedRadius = 0

    /**
     * 圆角边距
     */
    private var roundedMargin = 0

    /**
     * 圆角位置
     */
    private var cornerType: CornerType? = null

    /**
     * 是否需要灰度处理
     */
    private var isGrayScale = false

    /**
     * 是否需要高斯模糊处理
     */
    private var isBlur = false

    /**
     * 旋转角度
     */
    private var rotateDegree = 0

    /**
     * [ImageOptions.useAnimationPool]
     */
    private var useAnimationPool = false

    /**
     * 宽度和高度
     */
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    /**
     * 是否是圆形
     */
    private var isCropCircle = false

    /**
     * * 设置宽高比
     * 注意：由算法决定，必须是宽度与高度的比值
     * 即:width/height
     */
    var ratio = 0f

    /**
     * 是否自动以图片大小计算控件显示大小
     */
    var isAutoCalSize = false

    /**
     * 是否显示GIF
     */
    var isShowGif = false

    /**
     * glide图片缩放类型
     */
    var glideScaleType: GlideScaleType? = null

    /**
     * 加载监听器
     */
    private var listener: LoaderListener<*>? = null

    /**
     * 图片地址，可以是资源id(Integer) ，http地址及file文件路径
     */
    private var mUri: Any? = null

    @get:Direction
    @Direction
    var reverseDirection: Int = 0

    /**
     * 矩阵变化
     */
    var matrixValues: FloatArray? = null
    var sizeMultiplier: Float = 0f

    /**
     * 缓存策略
     */
    private var diskCacheStrategy: DiskCacheStrategy? = null
    private var srcCompat: Int = 0
    private val optionsBuilder: ImageOptions.Builder by lazy { ImageOptions.createBuilder(this) }
    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, defStyleAttr, 0)
        roundedRadius = a.getDimensionPixelSize(R.styleable.RatioImageView_riv_roundedRadius, 0)
        roundedMargin = a.getDimensionPixelSize(R.styleable.RatioImageView_riv_roundedMargin, 0)
        isGrayScale = a.getBoolean(R.styleable.RatioImageView_riv_grayScale, false)
        isCropCircle = a.getBoolean(R.styleable.RatioImageView_riv_isCropCircle, false)
        isBlur = a.getBoolean(R.styleable.RatioImageView_riv_isBlur, false)
        rotateDegree = a.getInteger(R.styleable.RatioImageView_riv_rotateDegree, 0)
        useAnimationPool = a.getBoolean(R.styleable.RatioImageView_riv_useAnimationPool, false)
        mWidth = a.getDimensionPixelSize(R.styleable.RatioImageView_riv_width, 0)
        mHeight = a.getDimensionPixelSize(R.styleable.RatioImageView_riv_height, 0)
        ratio = a.getFloat(R.styleable.RatioImageView_riv_ratio, 0.0f)
        val index = a.getInt(R.styleable.RatioImageView_riv_scaleType, -1)
        isAutoCalSize = a.getBoolean(R.styleable.RatioImageView_riv_autoSize, false)
        isShowGif = a.getBoolean(R.styleable.RatioImageView_riv_isShowGif, false)
        val reverseIndex = a.getInt(R.styleable.RatioImageView_riv_reverseDirection, -1)
        if (index >= 0) {
            setScaleType(S_SCALE_TYPE_ARRAY[index])
        }
        if (reverseIndex >= 1) {
            reverseDirection = S_REVERSE_DIRECTION[reverseIndex - 1]
        }
        placeholderDrawable =
            getDrawable(context, a.getResourceId(R.styleable.RatioImageView_riv_placeholder, -1))
        errorDrawable =
            getDrawable(context, a.getResourceId(R.styleable.RatioImageView_riv_error, -1))
        srcCompat = a.getResourceId(R.styleable.RatioImageView_srcCompat, -1)
        if (srcCompat == -1) {
            srcCompat = a.getResourceId(R.styleable.RatioImageView_riv_srcCompat, -1)
        }
        if (srcCompat != -1) {
            setImageUrl(srcCompat, isShowGif)
        }
        a.recycle()
    }

    fun getDrawable(context: Context?, resId: Int): Drawable? {
        if (resId == -1) {
            return null
        }
        var drawable: Drawable? = null
        try {
            drawable = AppCompatResources.getDrawable(context!!, resId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (drawable == null) {
            try {
                drawable = ResourcesCompat.getDrawable(resources, resId, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return drawable
    }

    fun setScaleType(scaleType: GlideScaleType?) {
        if (scaleType == null) {
            throw NullPointerException()
        }
        if (glideScaleType !== scaleType) {
            glideScaleType = scaleType
            setImageUrl(mUri)
        }
    }

    /**
     * 监听图片是否加载成功
     *
     * @param listener
     * @author dingpeihua
     * @date 2019/1/2 18:04
     * @version 1.0
     */
    open fun setListener(listener: LoaderListener<*>?) {
        this.listener = listener
    }

    /**
     * 宽高比
     * 注意：由算法决定，必须是宽度与高度的比值
     * 即:width/height
     *
     * @param width
     * @param height
     */
    open fun setRatio(width: Float, height: Float) {
        if (height == 0f || width == 0f) {
            ratio = -1f
            return
        }
        ratio = width / height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (ratio > 0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width / ratio).roundToInt()
            try {
                setMeasuredDimension(width, height)
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (isAutoCalSize) {
            // radio <0 高度随图片变
            val d = drawable
            if (d != null) {
                try {
                    // ceil not round - avoid thin vertical gaps along the left/right edges
                    val width = MeasureSpec.getSize(widthMeasureSpec)
                    //宽度定- 高度根据使得图片的宽度充满屏幕
                    val height = ceil(
                        (width.toFloat() * d.intrinsicHeight
                            .toFloat() / d.intrinsicWidth.toFloat()).toDouble()
                    ).toInt()
                    setMeasuredDimension(width, height)
                    return
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 根据文件对象加载图片
     *
     * @param url       需要加载的路径
     * @param isShowGif 是否加载GIF
     * @author dingpeihua
     * @date 2017/7/4 15:06
     * @version 1.0
     */
    fun setImageUrl(url: Any?, isShowGif: Boolean) {
        setImageUrl(url, isShowGif, 0, 0)
    }

    /**
     * 统一资源标志符加载图片,如果传入的宽和高大于0，则自动压缩图片以适应传入的宽和高
     *
     * @param uri       需要加载图片的统一资源标志符
     * @param isShowGif 是否加载GIF,加载gif 图片
     * @param width     图片压缩的宽度
     * @param height    图片压缩的高度
     * @author dingpeihua
     * @date 2017/7/4 15:07
     * @version 1.0
     */
    fun setImageUrl(uri: Any?, isShowGif: Boolean, width: Int, height: Int) {
        this.isShowGif = isShowGif
        setImageUrl(uri, width, height)
    }

    /**
     * 统一资源标志符加载图片,如果传入的宽和高大于0，则自动压缩图片以适应传入的宽和高
     *
     * @param uri    需要加载图片的统一资源标志符
     * @param width  图片压缩的宽度
     * @param height 图片压缩的高度
     * @author dingpeihua
     * @date 2017/7/4 15:07
     * @version 1.0
     */
    fun setImageUrl(uri: Any?, width: Int, height: Int) {
        if (uri == null) {
            return
        }
        this.mWidth = width
        this.mHeight = height
        setImageUrl(uri)
    }

    /**
     * 根据文件对象加载图片
     *
     * @param url      需要加载的路径
     * @param maxWidth 控件显示的最大宽度
     * @param ratio    图片真实宽高比
     * @author dingpeihua
     * @date 2017/7/4 15:06
     * @version 1.0
     */
    fun setImageUrl(url: Any?, maxWidth: Int, ratio: Float) {
        val newHeight = ceil((maxWidth.toFloat() * ratio).toDouble()).toInt()
        setImageUrl(url, maxWidth, newHeight)
    }

    /**
     * 统一资源标志符加载图片,如果传入的宽和高大于0，则自动压缩图片以适应传入的宽和高
     *
     * @param uri            需要加载图片的统一资源标志符
     * @param sizeMultiplier [ImageOptions.sizeMultiplier]
     * @author dingpeihua
     * @date 2017/7/4 15:07
     * @version 1.0
     */
    fun setImageUrl(uri: Any?, sizeMultiplier: Float) {
        if (uri == null) {
            return
        }
        this.sizeMultiplier = sizeMultiplier
        setImageUrl(uri)
    }

    fun builder(): ImageOptions.Builder {
        return optionsBuilder
    }

    /**
     * 统一资源标志符加载图片,如果传入的宽和高大于0，则自动压缩图片以适应传入的宽和高
     *
     * @param uri     需要加载图片的统一资源标志符
     * @param options 图片压缩的宽度
     * @author dingpeihua
     * @date 2017/7/4 15:07
     * @version 1.0
     */
    fun setImageUrl(uri: Any?) {
        if (uri == null) {
            return
        }
        if (Utils.isDestroy(context)) {
            return
        }
        mUri = uri
        optionsBuilder.setCropCircle(this.isCropCircle)
            .setRoundedRadius(this.roundedRadius)
            .setRoundedMargin(this.roundedMargin)
            .setBlur(this.isBlur)
            .setImageUrl(uri)
            .setGrayScale(this.isGrayScale)
            .setCornerType(this.cornerType)
            .setOverrideWidth(this.mWidth)
            .setOverrideHeight(this.mHeight)
            .setReverseDirection(this.reverseDirection)
            .setRtl(this.isRtl)
            .setSizeMultiplier(this.sizeMultiplier)
            .setLoaderListener(this.listener)
            .setMatrixValues(this.matrixValues)
            .setTargetView(this)
            .setShowGif(isShowGif)
            .setScaleType(glideScaleType)
            .setRotateDegree(rotateDegree.toFloat())
            .setUseAnimationPool(useAnimationPool)
            .setPlaceholderDrawable(placeholderDrawable)
            .setErrorPlaceholder(errorDrawable)
            .setDiskCacheStrategy(diskCacheStrategy)
        ImageLoader.getInstance().loadImage(optionsBuilder.build())
    }

    val isRtl: Boolean
        get() {
            val context = context
            val resources = context?.resources
            val configuration = resources?.configuration
            val layoutDirection = configuration?.layoutDirection
                ?: TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
            return layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
        }

    /**
     * 先判断bitmap 是否被回收
     * 避免Fatal Exception: java.lang.RuntimeException
     * Canvas: trying to use a recycled bitmap android.graphics.Bitmap
     *
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            if (bitmap == null || bitmap.isRecycled) {
                return
            }
        }
        try {
            super.onDraw(canvas)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun setPlaceholderDrawable(@DrawableRes placeholderResId: Int) {
        placeholderDrawable = ContextCompat.getDrawable(context, placeholderResId)
    }

    fun setErrorDrawable(@DrawableRes errorResId: Int) {
        errorDrawable = ContextCompat.getDrawable(context, errorResId)
    }

    fun setRoundedRadius(roundedRadius: Int) {
        this.roundedRadius = roundedRadius
    }

    fun setRoundedMargin(roundedMargin: Int) {
        this.roundedMargin = roundedMargin
    }

    fun setCornerType(cornerType: CornerType?) {
        this.cornerType = cornerType
    }

    fun setGrayScale(grayScale: Boolean) {
        isGrayScale = grayScale
    }

    fun setBlur(blur: Boolean) {
        isBlur = blur
    }

    fun setRotateDegree(rotateDegree: Int) {
        this.rotateDegree = rotateDegree
    }

    fun setUseAnimationPool(useAnimationPool: Boolean) {
        this.useAnimationPool = useAnimationPool
    }

    fun setCropCircle(cropCircle: Boolean) {
        isCropCircle = cropCircle
    }

    fun setDiskCacheStrategy(diskCacheStrategy: DiskCacheStrategy?) {
        this.diskCacheStrategy = diskCacheStrategy
    }

    companion object {
        /**
         * 垂直方向反转
         */
        const val REVERSE_VERTICAL = 1

        /**
         * 水平方向反转
         */
        const val REVERSE_HORIZONTAL = 2

        /**
         * 根据本地语言方向反转
         */
        const val REVERSE_LOCALE = 3
        private val S_SCALE_TYPE_ARRAY = arrayOf(
            GlideScaleType.FIT_CENTER,
            GlideScaleType.CENTER_INSIDE,
            GlideScaleType.CENTER_CROP,
            GlideScaleType.CIRCLE_CROP
        )
        private val S_REVERSE_DIRECTION = intArrayOf(
            REVERSE_VERTICAL, REVERSE_HORIZONTAL, REVERSE_LOCALE
        )
    }
}