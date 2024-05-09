package com.santeut.ui.mountain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import com.santeut.domain.usecase.MountainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(
    private val mountainUseCase: MountainUseCase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _mountains = MutableLiveData<List<MountainResponse>>()
    val mountains: LiveData<List<MountainResponse>> = _mountains

    fun searchMountain(name: String, region: String?) {
        viewModelScope.launch {
            try {
                val response = mountainUseCase.searchMountain(name, region)
                if (response.status == "200") {
                    _mountains.postValue(response.data.result)
                } else {
                    _error.postValue("산 목록 조회 실패: 상태 코드 ${response.status}")
                }
            } catch (e: Exception) {
                _error.postValue("산 목록 조회 실패: ${e.message}")
            }
        }
    }
}