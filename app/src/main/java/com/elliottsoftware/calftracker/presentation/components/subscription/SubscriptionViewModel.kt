package com.elliottsoftware.calftracker.presentation.components.subscription

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SubscriptionUiState(
    val isPremium: Boolean = false,
    val textData: List<String> = listOf("Unlimited calf storage. Offline usage. Cloud database backup")
)

class SubscriptionViewModel: ViewModel() {

    private var _uiState: MutableState<SubscriptionUiState> = mutableStateOf(SubscriptionUiState())
    val state = _uiState


    fun setIsPremium(value:Boolean){
        _uiState.value = _uiState.value.copy(isPremium = value)
    }
    fun setTextDataFree(){
        val textDataPremium = listOf("25 calf limit. Offline usage. Cloud database backup")
        _uiState.value = _uiState.value.copy(textData = textDataPremium)
    }
    fun setTextDataPremium(){
        val textDataPremium = listOf("Unlimited calf storage. Offline usage. Cloud database backup")
        _uiState.value = _uiState.value.copy(textData = textDataPremium)

    }
}