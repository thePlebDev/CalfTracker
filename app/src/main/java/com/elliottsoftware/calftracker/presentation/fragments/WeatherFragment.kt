package com.elliottsoftware.calftracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentWeatherBinding
/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherFragment : Fragment() {
    private var _binding:FragmentWeatherBinding? = null;
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }


}