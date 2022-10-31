package com.elliottsoftware.calftracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentNewCalfBinding
import com.elliottsoftware.calftracker.presentation.components.main.ScaffoldView
import com.elliottsoftware.calftracker.presentation.components.newCalf.NewCalfView


/**
 * A simple [Fragment] subclass.
 * Use the [NewCalfFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewCalfFragment : Fragment() {
    private var _binding:FragmentNewCalfBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewCalfBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.composeView.apply{
            setContent {
                NewCalfView()

            }

        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}