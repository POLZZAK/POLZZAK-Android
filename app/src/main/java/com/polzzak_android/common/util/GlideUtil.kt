package com.polzzak_android.common.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.loadImageUrl(imageUrl: String?) {
    Glide.with(this.context).load(imageUrl).into(this)
}

@BindingAdapter("imageDrawableRes")
fun ImageView.loadImageDrawableRes(@DrawableRes drawableRes: Int) {
    Glide.with(this.context).load(drawableRes).into(this)
}

@BindingAdapter("imageUrlCircleCrop")
fun ImageView.loadCircleImageUrl(imageUrl: String?) {
    Glide.with(this.context).load(imageUrl).circleCrop().into(this)
}

@BindingAdapter("imageDrawableResCircleCrop")
fun ImageView.loadCircleImageDrawableRes(@DrawableRes drawableRes: Int) {
    Glide.with(this.context).load(drawableRes).circleCrop().into(this)
}