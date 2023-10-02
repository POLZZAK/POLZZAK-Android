package com.polzzak_android.presentation.common.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.loadImageUrl(imageUrl: String?, @DrawableRes placeHolderRes: Int? = null) {
    Glide.with(this.context).load(imageUrl).run {
        placeHolderRes?.let { placeholder(placeHolderRes) } ?: this
    }.into(this)
}

fun ImageView.loadImageDrawableRes(
    @DrawableRes drawableRes: Int,
    @DrawableRes placeHolderRes: Int? = null
) {
    Glide.with(this.context).load(drawableRes).run {
        placeHolderRes?.let { placeholder(placeHolderRes) } ?: this
    }.into(this)
}

fun ImageView.loadCircleImageUrl(imageUrl: String?, @DrawableRes placeHolderRes: Int? = null) {
    Glide.with(this.context).load(imageUrl).circleCrop().run {
        placeHolderRes?.let { placeholder(placeHolderRes) } ?: this
    }.into(this)
}

fun ImageView.loadCircleImageDrawableRes(
    @DrawableRes drawableRes: Int,
    @DrawableRes placeHolderRes: Int? = null
) {
    Glide.with(this.context).load(drawableRes).circleCrop().run {
        placeHolderRes?.let { placeholder(placeHolderRes) } ?: this
    }.into(this)
}