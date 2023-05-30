package com.polzzak_android.common.util.livedata

/**
 * LiveData 단일 이벤트 발송용 wrapper class
 * EventWrapperObserver로 observe할 경우 이벤트를 발송했다면 다시 이벤트를 발송하지 않음(ex - backstack에서 돌아와 다시 setObserve할 때)
 * @param content 관찰할 데이터
 **/
open class EventWrapper<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

