package com.elliottsoftware.calftracker.presentation.components.subscription

import android.content.ServiceConnection
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.background.BillingService
import com.elliottsoftware.calftracker.data.repositories.BillingRepository
import com.elliottsoftware.calftracker.presentation.viewModels.MainUIState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

data class SubscriptionUiState(
    val isPremium: Boolean = false,
    val textData: List<String> = listOf("Unlimited calf storage. Offline usage. Cloud database backup")
)

class SubscriptionViewModel(
    val billingRepository: BillingRepository = BillingRepository()
): ViewModel() {

//    private var _uiState: MutableState<SubscriptionUiState> = mutableStateOf(SubscriptionUiState())
//    val state: State<SubscriptionUiState> = _uiState

    private var _uiState: MutableState<Int> = mutableStateOf(2)
    val state: State<Int> = _uiState
    val gettingStuff = billingRepository.getStuff()
     fun serviceConnection():ServiceConnection{
        return billingRepository.getServiceConnection()
    }
    init{
        getRandomNums()

    }

    fun getRandomNums() = viewModelScope.launch {
        billingRepository.getStuff().collect{
            _uiState.value = it
        }
    }





}