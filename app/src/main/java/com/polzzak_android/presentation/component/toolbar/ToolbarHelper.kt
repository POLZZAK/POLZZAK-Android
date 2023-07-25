package com.polzzak_android.presentation.component.toolbar

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton

/**
 * 데이터(data model)와 뷰(xml)를 바인딩하여 툴바를 세팅하는 helper class
 * 데이터는 필수고, 해당 데이터와 바인딩할 뷰를 세팅.
 * @see ToolbarData
 * @see layout_toolbar.xml
 */
class ToolbarHelper(
    private val data: ToolbarData,
    private val backButtonView: AppCompatImageButton? = null,
    private val titleView: TextView? = null,
    private val textIconView: AppCompatButton? = null,
    private val imageIconView: AppCompatImageButton? = null
) {
    fun set() {
        // 백 버튼
        backButtonView?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                data.popStack?.popBackStack()
            }
        }

        // 타이틀
        titleView?.text = data.titleText

        // 텍스트 아이콘
        textIconView?.apply {
            visibility = View.VISIBLE
            text = data.iconText
            setOnClickListener {
                data.iconInteraction?.onToolbarIconClicked()
            }
        }

        // 이미지 아이콘
        imageIconView?.apply {
            visibility = View.VISIBLE
            data.iconImageId?.let {
                imageIconView.setImageResource(it)
            }
            setOnClickListener {
                data.iconInteraction?.onToolbarIconClicked()
            }
        }
    }
}