package com.elliottsoftware.calftracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentCalfStatsBinding
import com.elliottsoftware.calftracker.presentation.components.editCalf.EditCalfView


/**
 * A simple [Fragment] subclass.
 * Use the [CalfStatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalfStatsFragment : Fragment() {

    private var _binding: FragmentCalfStatsBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalfStatsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                Text("THIS IS THE PAGE WHERE THERE IS GOING TO BE A LARGE CIRCLE GRAPHIC")


            }
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}