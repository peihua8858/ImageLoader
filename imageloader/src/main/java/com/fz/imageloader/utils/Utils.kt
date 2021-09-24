package com.fz.imageloader.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.lang.AssertionError

/**
 * 图片加载工具类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/6/17 15:13
 */
class Utils private constructor() {
    companion object {
        /**
         * 查找当前上下文activity
         *
         * @param context
         * @author dingpeihua
         * @date 2019/11/19 15:38
         * @version 1.0
         */
        @JvmStatic
        fun findActivity(context: Context): Activity? {
            if (context is Activity) {
                if (!context.isFinishing) {
                    return context
                }
            } else if (context is ContextWrapper) {
                return findActivity(context.baseContext)
            }
            return null
        }

        /**
         * 判断activity是否销毁
         *
         * @param context
         * @author dingpeihua
         * @date 2019/11/19 15:39
         * @version 1.0
         */
        @JvmStatic
        fun isDestroy(context: Context): Boolean {
            val activity = findActivity(context)
            return activity == null || activity.isDestroyed || activity.isFinishing
        }
    }

    init {
        throw AssertionError()
    }
}