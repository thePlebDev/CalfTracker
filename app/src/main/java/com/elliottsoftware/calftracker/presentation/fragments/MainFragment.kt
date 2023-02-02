package com.elliottsoftware.calftracker.presentation.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.elliottsoftware.calftracker.util.findActivity


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding.root
        val sharedViewModel: EditCalfViewModel by activityViewModels()
        val mainViewModel: MainViewModel by activityViewModels()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val activity = activity?.findActivity()!!

        binding.composeView.apply{
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val windowSize = calculateWindowSizeClass(activity).widthSizeClass
                MainView(
                    viewModel = mainViewModel,
                    onNavigate = { dest -> findNavController().navigate(dest) },
                    sharedViewModel = sharedViewModel,
                    windowSize
                )


            }

        }




        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}