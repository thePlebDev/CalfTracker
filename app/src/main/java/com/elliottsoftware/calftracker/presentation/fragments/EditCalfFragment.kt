package com.elliottsoftware.calftracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentEditCalfBinding
import com.elliottsoftware.calftracker.databinding.FragmentNewCalfBinding
import com.elliottsoftware.calftracker.presentation.components.editCalf.EditCalfView
import com.elliottsoftware.calftracker.presentation.components.newCalf.NewCalfView
import androidx.fragment.app.activityViewModels
import com.elliottsoftware.calftracker.presentation.viewModels.EditCalfViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [EditCalfFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditCalfFragment : Fragment() {
    private var _binding:FragmentEditCalfBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditCalfBinding.inflate(inflater,container,false)
        val view = binding.root
        val sharedViewModel: EditCalfViewModel by activityViewModels()
        binding.composeView.apply{
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                EditCalfView(sharedViewModel)

            }

        }
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
