package com.fz.imageloader.glide;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.socks.library.KLog;

public class RatioImageView extends com.fz.imageloader.widget.RatioImageView {
    public RatioImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, defStyleAttr, 0);
        int srcCompat = a.getResourceId(R.styleable.RatioImageView_riv_srcCompat, -1);
        KLog.d("RatioImageView>>>>>>>>>>>$srcCompat");
        setImageUrl(srcCompat, isShowGif());
        a.recycle();
    }

    private void loadImage(Context context, AttributeSet attrs,int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView, defStyleAttr, 0);
        int srcCompat = a.getResourceId(R.styleable.RatioImageView_riv_srcCompat, -1);
        KLog.d("RatioImageView>>>>>>>>>>>$srcCompat");
        setImageUrl(srcCompat, isShowGif());
        a.recycle();
    }

    public RatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RatioImageView(@NonNull Context context) {
        this(context,null);
//        loadImage(context,null,0);
    }
}
