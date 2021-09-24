package com.fz.imageloader.picasso.transformations

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import com.squareup.picasso.Transformation

//class FitCenter: Transformation {
//    private val ID = "com.fz.imageloader.picasso.transformations.FitCenter"
//    override fun transform(source: Bitmap?): Bitmap {
//return null
//    }
//    fun fitCenter(inBitmap: Bitmap): Bitmap {
//        if (inBitmap.width == width && inBitmap.height == height) {
//            if (Log.isLoggable(TransformationUtils.TAG, Log.VERBOSE)) {
//                Log.v(
//                    TransformationUtils.TAG,
//                    "requested target size matches input, returning input"
//                )
//            }
//            return inBitmap
//        }
//        val widthPercentage = width / inBitmap.width.toFloat()
//        val heightPercentage = height / inBitmap.height.toFloat()
//        val minPercentage = Math.min(widthPercentage, heightPercentage)
//
//        // Round here in case we've decoded exactly the image we want, but take the floor below to
//        // avoid a line of garbage or blank pixels in images.
//        var targetWidth = Math.round(minPercentage * inBitmap.width)
//        var targetHeight = Math.round(minPercentage * inBitmap.height)
//        if (inBitmap.width == targetWidth && inBitmap.height == targetHeight) {
//            if (Log.isLoggable(TransformationUtils.TAG, Log.VERBOSE)) {
//                Log.v(
//                    TransformationUtils.TAG,
//                    "adjusted target size matches input, returning input"
//                )
//            }
//            return inBitmap
//        }
//
//        // Take the floor of the target width/height, not round. If the matrix
//        // passed into drawBitmap rounds differently, we want to slightly
//        // overdraw, not underdraw, to avoid artifacts from bitmap reuse.
//        targetWidth = (minPercentage * inBitmap.width).toInt()
//        targetHeight = (minPercentage * inBitmap.height).toInt()
//        val config: Bitmap.Config = TransformationUtils.getNonNullConfig(inBitmap)
//        val toReuse: Bitmap = pool.get(targetWidth, targetHeight, config)
//
//        // We don't add or remove alpha, so keep the alpha setting of the Bitmap we were given.
//        TransformationUtils.setAlpha(inBitmap, toReuse)
//        if (Log.isLoggable(TransformationUtils.TAG, Log.VERBOSE)) {
//            Log.v(TransformationUtils.TAG, "request: " + width + "x" + height)
//            Log.v(TransformationUtils.TAG, "toFit:   " + inBitmap.width + "x" + inBitmap.height)
//            Log.v(TransformationUtils.TAG, "toReuse: " + toReuse.width + "x" + toReuse.height)
//            Log.v(TransformationUtils.TAG, "minPct:   $minPercentage")
//        }
//        val matrix = Matrix()
//        matrix.setScale(minPercentage, minPercentage)
//        TransformationUtils.applyMatrix(inBitmap, toReuse, matrix)
//        return toReuse
//    }
//    override fun key(): String {
//       return ID
//    }
//}