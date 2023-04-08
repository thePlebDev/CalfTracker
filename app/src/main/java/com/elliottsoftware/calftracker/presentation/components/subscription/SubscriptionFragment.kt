package com.elliottsoftware.calftracker.presentation.components.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentSubscriptionBinding
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [SubscriptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubscriptionFragment : Fragment() {

    private var _binding: FragmentSubscriptionBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onDetach() {
        super.onDetach()
        Timber.tag("CLOSINGT").d("fragment DETACHED")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("CLOSINGT").d("fragment destroyed")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag("CLOSINGT").d("fragment CREATED")
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        val view = binding.root


        //viewModels() should be used instead of activityViewModels()
        val billingViewModel:BillingViewModel by activityViewModels()

        lifecycle.addObserver(billingViewModel)

        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {
                    SubscriptionView(
                        onNavigate = { dest -> findNavController().navigate(dest) },
                        viewModel = billingViewModel
                    )
                }
            }
        }
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        lifecycle.removeObserver(billingViewModel)
    }



}