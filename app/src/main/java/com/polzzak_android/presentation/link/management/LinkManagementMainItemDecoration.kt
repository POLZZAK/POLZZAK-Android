package com.polzzak_android.presentation.link.management

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.polzzak_android.presentation.common.util.BindableItemAdapter

class LinkManagementMainItemDecoration(
    @Px private val marginPx: Int
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
        if (position == adapter.currentList.lastIndex) {
            outRect.bottom = marginPx
        }
        outRect.top = marginPx
    }
}