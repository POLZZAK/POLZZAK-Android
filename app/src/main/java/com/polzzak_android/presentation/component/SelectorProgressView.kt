package com.polzzak_android.presentation.component

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import com.polzzak_android.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * 2개의 drawable로 진행도를 표시(ex. 회원가입, 온보딩 진행도 등)
 * @property maxCount drawable의 최대 개수
 * @property selectedCount selectedDrawable을 적용시킬 개수
 * @property selectedDrawableRes selectedCount만큼 앞에 해당 drawable로 채움
 * @property unSelectedDrawableRes maxCount - selectedCount 만큼 뒤에 해당 drawable로 채움
 * @property drawableWidthPx 각 drawable의 너비 px
 * @property drawableHeightPx 각 drawable의 높이 px
 * @property betweenMarginsPx 각 drawable의 사이 margin px
 */
class SelectorProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var maxCount by ViewAttributeDelegate(initValue = 0)
    var selectedCount by ViewAttributeDelegate(
        initValue = 0,
        getter = { value -> minOf(maxCount, value) })

    @delegate:DrawableRes
    var selectedDrawableRes by ViewAttributeDelegate<Int?>(initValue = null)

    @delegate:DrawableRes
    var unSelectedDrawableRes by ViewAttributeDelegate<Int?>(initValue = null)

    @delegate:Px
    var drawableWidthPx by ViewAttributeDelegate(initValue = 0)

    @delegate:Px
    var drawableHeightPx by ViewAttributeDelegate(initValue = 0)

    @delegate:Px
    var betweenMarginsPx by ViewAttributeDelegate(initValue = 0)

    private var isInitialized = false

    init {
        isInitialized = true
        val typedArray =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SelectorProgressView,
                defStyleAttr,
                0
            )

        maxCount = typedArray.getInteger(R.styleable.SelectorProgressView_maxCount, 0)
        selectedCount = typedArray.getInteger(R.styleable.SelectorProgressView_selectedCount, 0)
        selectedDrawableRes =
            typedArray.getResourceId(R.styleable.SelectorProgressView_selectedDrawable, -1)
                .takeIf { it != -1 }
        unSelectedDrawableRes =
            typedArray.getResourceId(R.styleable.SelectorProgressView_unSelectedDrawable, -1)
                .takeIf { it != -1 }
        drawableWidthPx =
            typedArray.getDimensionPixelOffset(R.styleable.SelectorProgressView_drawableWidth, 0)
        drawableHeightPx =
            typedArray.getDimensionPixelOffset(R.styleable.SelectorProgressView_drawableHeight, 0)
        betweenMarginsPx =
            typedArray.getDimensionPixelOffset(R.styleable.SelectorProgressView_betweenMargins, 0)
        typedArray.recycle()
        isInitialized = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width =
            if (widthMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(widthMeasureSpec) else maxCount * drawableWidthPx + (maxCount - 1) * betweenMarginsPx
        val height =
            if (heightMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(heightMeasureSpec) else drawableHeightPx

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { drawView(canvas = it) }
    }

    private fun drawView(canvas: Canvas) {
        val selectedDrawable =
            selectedDrawableRes?.let { ContextCompat.getDrawable(context, it) }
        val unSelectedDrawable =
            unSelectedDrawableRes?.let { ContextCompat.getDrawable(context, it) }
        var left = 0
        repeat(maxCount) {
            val target = if (it >= selectedCount) unSelectedDrawable else selectedDrawable
            target?.setBounds(left, 0, left + drawableWidthPx, drawableHeightPx)
            target?.draw(canvas)
            left += betweenMarginsPx + drawableWidthPx
        }
    }

    private inner class ViewAttributeDelegate<T>(
        initValue: T,
        private val getter: ((T) -> T)? = null
    ) : ReadWriteProperty<Any?, T> {
        private var value = initValue
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return getter?.invoke(value) ?: value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = value
            if (isInitialized) {
                requestLayout()
                invalidate()
            }
        }
    }
}