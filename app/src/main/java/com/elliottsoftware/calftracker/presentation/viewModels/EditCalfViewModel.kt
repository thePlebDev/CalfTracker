package com.elliottsoftware.calftracker.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf

class EditCalfViewModel:ViewModel() {

    private val _state: MutableState<FireBaseCalf> = mutableStateOf(FireBaseCalf())
    val state:State<FireBaseCalf> = _state

    fun setCalf(calf:FireBaseCalf){
        _state.value = calf
    }
}