package com.polzzak_android.presentation.component

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation

class ScrollSwitchLinearLayoutManager @JvmOverloads constructor(
    context: Context,
    @Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    var isScrollable: Boolean = true

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollable
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && isScrollable
    }
}