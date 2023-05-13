package com.elliottsoftware.calftracker.presentation.components.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
//import androidx.compose.material.AppBar
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.databinding.FragmentMainBinding
import com.elliottsoftware.calftracker.presentation.components.main.*
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainViewModel

import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.elliottsoftware.calftracker.background.BillingService
import com.elliottsoftware.calftracker.presentation.components.billing.BillingClientWrapper
import com.elliottsoftware.calftracker.presentation.components.subscription.BillingViewModel
import com.elliottsoftware.calftracker.presentation.components.subscription.SubscriptionViewModel
import com.elliottsoftware.calftracker.presentation.viewModels.MainUIState
import com.elliottsoftware.calftracker.presentation.viewModels.NewCalfViewModel
import com.elliottsoftware.calftracker.util.findActivity
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainFragment() : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!






   private val subscriptionViewModel: SubscriptionViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val billingViewModel: BillingViewModel by activityViewModels()
        lifecycle.addObserver(billingViewModel)

        _binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding.root
        val sharedViewModel: EditCalfViewModel by activityViewModels()
        val mainViewModel: MainViewModel by activityViewModels()
        val newCalfViewModel: NewCalfViewModel by activityViewModels()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val activity = activity?.findActivity()!!

        binding.composeView.apply{
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                MainView(
                    viewModel = mainViewModel,
                    onNavigate = { dest -> findNavController().navigate(dest) },
                    sharedViewModel = sharedViewModel,
                    billingViewModel = billingViewModel,
                    newCalfViewModel = newCalfViewModel
                )
//                Column() {
//                    val data =subscriptionViewModel.state.value
//                    Text("$data", fontSize = 60.sp)
//                    Button(onClick = { /*TODO*/ }) {
//                        Text("Make the button move up")
//                    }
//
//                }




            }

        }




        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //I don't think we have to remove this. I think it is done automatically for us.
        //lifecycle.removeObserver(billingViewModel)
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService.
//        Intent(this.requireContext(), BillingService::class.java).also { intent ->
//            activity?.bindService(intent, subscriptionViewModel.serviceConnection(), Context.BIND_AUTO_CREATE)
//        }

    }
    override fun onStop() {
        super.onStop()
       // activity?.unbindService(subscriptionViewModel.serviceConnection())

    }


}