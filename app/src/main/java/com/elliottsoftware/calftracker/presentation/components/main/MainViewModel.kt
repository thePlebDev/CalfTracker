package com.elliottsoftware.calftracker.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliottsoftware.calftracker.domain.models.Response
import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.useCases.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


data class MainUIState(
    //val loggedUserOut:Boolean = false,
    val data:Response<List<FireBaseCalf>> = Response.Loading,
    val darkTheme:Boolean = false,
    val chipText:List<String> = listOf("TOTAL: 0 ","BULLS: 0","HEIFERS: 0","VACCINATED: 0"),
    val showDeleteModal:Boolean = false,
    val calfToBeDeletedTagNumber:String = "",
    val calfToBeDeletedId:String = "",
    val animate:Boolean = false,
    val calfLimit:Long = 50,
    val disablePaginationButton:Boolean = false,
    val paginationState:Response<Boolean> = Response.Success(true),
    val currentLength: Int = 0

        )

@HiltViewModel
class MainViewModel @Inject constructor(
   private val logoutUseCase: LogoutUseCase,
   private val getCalvesUseCase: GetCalvesUseCase,
   private val deleteCalfUseCase: DeleteCalfUseCase,
   private val getCalfByTagNumberUseCase: GetCalfByTagNumberUseCase,
   private val paginatedCalfQuery: PaginatedCalfQuery,

):ViewModel() {


    private var _uiState: MutableState<MainUIState> = mutableStateOf(MainUIState())
    val state:State<MainUIState> = _uiState
    private val auth: FirebaseAuth = Firebase.auth
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO


    init{

        getCalves()
    }

    fun signUserOut()= viewModelScope.launch{
        //state.value = state.value.copy(loggedUserOut = logoutUseCase.invoke())
        logoutUseCase.execute(Unit)

    }


     fun getCalves() = viewModelScope.launch(){


        getCalvesUseCase.execute(
            GetCalvesParams(
                calfLimit = _uiState.value.calfLimit,
                userEmail = auth.currentUser?.email!!
            )
        )
            .flowOn(dispatcherIO)
            .collect{response ->
            _uiState.value = _uiState.value.copy(
                data = response,
            )
        }

    }
    fun cancelSearch(){
        getCalves()
    }

    fun getPaginatedQuery() = viewModelScope.launch{
        val limit =_uiState.value.calfLimit
        var disableButton = false

        paginatedCalfQuery.execute(limit)
            .flowOn(dispatcherIO)
            .collect{ response ->
            when(response){ // need to get rid of this and just do it in the UI, we just do simple collect, respone and calfLimit
                is Response.Loading ->{
                    _uiState.value = _uiState.value.copy(
                        paginationState = Response.Loading,
                    )

                }
                is Response.Success ->{

                   val paginatedCalfList = response.data

                    if(_uiState.value.currentLength == paginatedCalfList.size){
                        disableButton = true
                    }

                    _uiState.value = _uiState.value.copy(
                        calfLimit = _uiState.value.calfLimit + 20, // this stays
                        data = Response.Success(paginatedCalfList),// not sure about this
                        disablePaginationButton = disableButton, // gone
                        currentLength = paginatedCalfList.size,
                        paginationState = Response.Success(true)
                    )
                }
                is Response.Failure ->{
                    _uiState.value = _uiState.value.copy(
                        paginationState = Response.Failure(Exception()),
                    )
                }
            }

        }


    }

    fun deleteCalf(id:String) = viewModelScope.launch{
        deleteCalfUseCase.execute(
            DeleteCalfParams(calfId =id, userEmail =auth.currentUser?.email!! )
        )
            .flowOn(dispatcherIO)
            .collect{ response ->

        }


    }
    fun setDarkMode(){
        _uiState.value = _uiState.value.copy(darkTheme = !_uiState.value.darkTheme)
    }



    fun searchCalfListByTag(tagNumber:String) = viewModelScope.launch{
        getCalfByTagNumberUseCase.execute(tagNumber)
            .flowOn(dispatcherIO)
            .collect{ response ->
            _uiState.value = _uiState.value.copy(data = response)
        }

    }


    fun setChipText(calfList:List<FireBaseCalf>){
        val total = calfList.size
        val bulls = calfList.count{it.sex == "Bull"}
        val heifers = calfList.count{it.sex == "Heifer"}
        val vaccinated = calfList.count{it.vaccinelist != null && it.vaccinelist!!.isNotEmpty()}

        val chipTextList:List<String> = listOf("TOTAL: $total ","BULLS: $bulls","HEIFERS: $heifers","VACCINATED: $vaccinated")
        _uiState.value = _uiState.value.copy(chipText = chipTextList)
    }

    fun setShowDeleteModal(value: Boolean){
        _uiState.value = _uiState.value.copy(showDeleteModal = value)

    }
    fun setCalfDeleteTagNId(tagNumber:String,id:String){
        _uiState.value = _uiState.value.copy(calfToBeDeletedTagNumber = tagNumber,calfToBeDeletedId = id)
    }






}