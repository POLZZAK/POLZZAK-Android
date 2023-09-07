package com.polzzak_android.presentation.feature.myPage.notice

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.polzzak_android.presentation.common.util.BindableItemAdapter
import com.polzzak_android.presentation.feature.myPage.notice.item.MyNoticeItem

class MyNoticeItemDecoration(@Px private val marginPx: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val adapter = (parent.adapter as? BindableItemAdapter) ?: return
        if (adapter.currentList.getOrNull(position) !is MyNoticeItem) return
        outRect.top = marginPx
        outRect.bottom = if (position == adapter.currentList.lastIndex) marginPx else 0
    }
}