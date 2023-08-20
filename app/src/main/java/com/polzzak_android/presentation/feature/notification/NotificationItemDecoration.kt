package com.polzzak_android.presentation.feature.notification

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class NotificationItemDecoration(
    @Px private val paddingPx: Int,
    @Px private val betweenMarginPx: Int,
    private val offset: Int = 0
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position < offset) return
        outRect.top = if (position == offset) paddingPx else 0
        outRect.left = paddingPx
        outRect.right = paddingPx
        outRect.bottom = if (position == parent.childCount - 1) paddingPx else betweenMarginPx
    }
}