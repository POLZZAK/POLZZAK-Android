package com.polzzak_android.presentation.feature.link.search

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.feature.link.item.LinkMainSentRequestItem

class SearchMainItemDecoration(
    @Px private val betweenMarginPx: Int,
    @Px private val bottomMarginPx: Int
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
        val marginPx = when (adapter.currentList.getOrNull(position)) {
            is LinkMainSentRequestItem -> {
                if (position >= adapter.currentList.lastIndex) bottomMarginPx
                else betweenMarginPx
            }

            else -> 0
        }
        outRect.bottom = marginPx
    }
}