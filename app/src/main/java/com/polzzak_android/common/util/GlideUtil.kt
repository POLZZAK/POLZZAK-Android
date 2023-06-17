package com.polzzak_android.common.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.loadImageUrl(imageUrl: String?) {
    Glide.with(this.context).load(imageUrl).into(this)
}

@BindingAdapter("imageUrl")
fun ImageView.loadCircleImageUrl(imageUrl: String?) {
    Glide.with(this.context).load(imageUrl).circleCrop().into(this)
}