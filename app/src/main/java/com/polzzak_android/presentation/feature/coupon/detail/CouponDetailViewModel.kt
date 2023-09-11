package com.polzzak_android.presentation.feature.coupon.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.repository.CouponRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.coupon.model.CouponDetailModel
import com.polzzak_android.presentation.feature.coupon.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CouponDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CouponRepository
) : ViewModel() {
    private val _couponDetailData = MutableStateFlow<ModelState<CouponDetailModel>>(ModelState.Loading())
    val couponDetailData
        get() = _couponDetailData.asStateFlow()

    init {
        val token = savedStateHandle.get<String>("token") ?: ""
        val couponId = savedStateHandle.get<Int>("couponId") ?: -1
        fetchCouponDetailData(accessToken = token, couponId = couponId)
    }

    fun fetchCouponDetailData(
        accessToken: String,
        couponId: Int
    ) = viewModelScope.launch {
        repository
            .getCouponDetail(token = accessToken, couponId = couponId)
            .onSuccess { data ->
                data ?: return@onSuccess

                _couponDetailData.update {
                    ModelState.Success(data.toModel())
                }
            }
            .onError { exception, data ->
                Timber.e(exception)

                _couponDetailData.update {
                    ModelState.Error(exception = exception)
                }
            }
    }

    fun requestReward(couponId: Int) {
        // TODO: 조르기 api 호출 처리
    }

    fun receiveReward(couponId: Int) {
        // TODO: 보상 받음 api 호출 처리
    }
 }