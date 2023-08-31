package com.polzzak_android.presentation.component.toolbar

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.polzzak_android.databinding.LayoutToolbarBinding

/**
 * 데이터(data model)와 뷰(xml)를 바인딩하여 툴바를 세팅하는 helper class
 * @see ToolbarData
 * @see layout_toolbar.xml
 */
class ToolbarHelper(
    private val data: ToolbarData,
    private val toolbar: LayoutToolbarBinding
) {
    private val backButtonView = toolbar.toolbarBackButton
    private val titleView = toolbar.toolbarTitle
    private val textIconView = toolbar.toolbarTextIcon
    private val imageIconView = toolbar.toolbarImageIcon

    /**
     * 데이터 및 가시성 세팅
     */
    fun set() {
        // 백 버튼
        backButtonView.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                data.popStack?.popBackStack()
            }
        }

        // 타이틀
        titleView.text = data.titleText

        // 텍스트 아이콘
        textIconView.apply {
            visibility = View.VISIBLE
            text = data.iconText
            setOnClickListener {
                data.iconInteraction?.onToolbarIconClicked()
            }
        }

        // 이미지 아이콘
        imageIconView.apply {
            visibility = View.VISIBLE
            data.iconImageId?.let {
                imageIconView.setImageResource(it)
            }
            setOnClickListener {
                data.iconInteraction?.onToolbarIconClicked()
            }
        }
    }

    fun hideBackButton() {
        backButtonView.visibility = View.GONE
    }

    fun updateToolbarBackgroundColor(colorResource: Int) {
        toolbar.root.setBackgroundResource(colorResource)
    }

    fun updateBackButtonColor(colorResource: Int) {
        backButtonView.imageTintList = AppCompatResources.getColorStateList(backButtonView.context, colorResource)
    }

    fun updateTitleColor(colorResource: Int) {
        titleView.setTextColor(titleView.context.getColor(colorResource))
    }


    fun updateTextIconColor(colorResource: Int) {
        textIconView.setTextColor(textIconView.context.getColor(colorResource))
    }

    fun updateImageIconColor(colorResource: Int) {
        imageIconView.imageTintList = AppCompatResources.getColorStateList(imageIconView.context, colorResource)

    }
}