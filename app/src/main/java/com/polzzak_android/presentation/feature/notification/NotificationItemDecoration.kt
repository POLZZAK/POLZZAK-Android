package com.polzzak_android.presentation.feature.notification

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.feature.notification.item.NotificationItem
import com.polzzak_android.presentation.feature.notification.item.NotificationSkeletonLoadingItem

class NotificationItemDecoration(
    @Px private val paddingPx: Int,
    @Px private val betweenMarginPx: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = (parent.adapter as? BindableItemAdapter) ?: return
        val position = parent.getChildAdapterPosition(view)
        val currentItem = adapter.currentList.getOrNull(position)
        if (!isContentItem(currentItem)) return
        val prevItem = adapter.currentList.getOrNull(position - 1)
        val nextItem = adapter.currentList.getOrNull(position + 1)
        outRect.top =
            if (isContentItem(prevItem)) 0 else paddingPx
        outRect.left = paddingPx
        outRect.right = paddingPx
        outRect.bottom =
            if (isContentItem(nextItem)) betweenMarginPx else paddingPx
    }

    private fun isContentItem(item: Any?): Boolean =
        item is NotificationItem || item is NotificationSkeletonLoadingItem
}