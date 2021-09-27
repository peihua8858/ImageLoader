package com.fz.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView

abstract class ImageViewTarget<R>(val imageView: ImageView) {

    /**
     * A lifecycle callback that is called when a load is started.
     *
     *
     * Note - This may not be called for every load, it is possible for example for loads to fail
     * before the load starts (when the model object is null).
     *
     *
     * Note - This method may be called multiple times before any other lifecycle method is called.
     * Loads can be paused and restarted due to lifecycle or connectivity events and each restart may
     * cause a call here.
     *
     * @param placeholder The placeholder drawable to optionally show, or null.
     */
    abstract fun onLoadStarted(placeholder: Drawable?)

    /**
     * A **mandatory** lifecycle callback that is called when a load fails.
     *
     *
     * Note - This may be called before [.onLoadStarted]
     * if the model object is null.
     *
     *
     * You **must** ensure that any current Drawable received in [.onResourceReady] is no longer used before redrawing the container (usually a View) or changing its
     * visibility.
     *
     * @param errorDrawable The error drawable to optionally show, or null.
     */
    abstract fun onLoadFailed(errorDrawable: Drawable?)

    /**
     * The method that will be called when the resource load has finished.
     *
     * @param resource the loaded resource.
     */
    abstract fun onResourceReady(resource: R?)

    /**
     * A **mandatory** lifecycle callback that is called when a load is cancelled and its resources
     * are freed.
     *
     *
     * You **must** ensure that any current Drawable received in [.onResourceReady] is no longer used before redrawing the container (usually a View) or changing its
     * visibility.
     *
     * @param placeholder The placeholder drawable to optionally show, or null.
     */
    abstract fun onLoadCleared(placeholder: Drawable?)

}