package com.polzzak_android.presentation.feature.coupon.main.protector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polzzak_android.data.remote.model.response.toCoupon
import com.polzzak_android.data.repository.CouponRepository
import com.polzzak_android.presentation.common.model.ModelState
import com.polzzak_android.presentation.feature.coupon.model.Coupon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val repository: CouponRepository
) : ViewModel() {

    private val _couponList: MutableLiveData<ModelState<List<Coupon>>> = MutableLiveData()
    val couponList get() = _couponList

    fun requestCouponList(token: String, couponState: String, memberId: Int?) {
        viewModelScope.launch {
            _couponList.value = ModelState.Loading()
            repository.getCouponList(token, couponState, memberId).onSuccess { couponList ->
                _couponList.value = ModelState.Success(couponList!!.map { it.toCoupon() })
            }.onError { exception, data ->
                _couponList.value = ModelState.Error(exception)
            }
        }
    }
}