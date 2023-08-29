package com.polzzak_android.presentation.feature.notification.setting

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.polzzak_android.presentation.common.util.BindableItemAdapter

class NotificationSettingItemDecoration(
    @ColorInt private val allMenuDividerColor: Int,
    @ColorInt private val otherMenuDividerColor: Int,
    @Px private val heightPx: Int
) : ItemDecoration() {
    private val allMenuPaint = Paint().apply {
        color = allMenuDividerColor
    }
    private val otherMenuPaint = Paint().apply {
        color = otherMenuDividerColor
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = (parent.adapter as? BindableItemAdapter) ?: return
        val left = (parent.paddingStart).toFloat()
        val right = (parent.run { width - paddingEnd }).toFloat()
        parent.children.forEachIndexed { index, view ->
            val position = parent.getChildAdapterPosition(view)
            if (position == adapter.currentList.lastIndex) return@forEachIndexed
            val top = view.bottom.toFloat()
            val bottom = (top + heightPx)
            val paint = if (position == 0) allMenuPaint else otherMenuPaint
            c.drawRect(left, top, right, bottom, paint)
        }
    }
}