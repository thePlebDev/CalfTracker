package com.elliottsoftware.calftracker.presentation.components.editCalf

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun EditCalfView(viewModel: EditCalfViewModel){

    val state = viewModel.state.value
    Column() {
        Text("Calf tag number ->" + state.calfTag)
        Text("Cow tag number ->" + state.cowTag)
        Text("Details ->" + state.details)
        Text("Sex ->" + state.sex)
    }

}