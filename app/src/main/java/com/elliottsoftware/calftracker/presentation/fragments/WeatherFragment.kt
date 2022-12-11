package com.elliottsoftware.calftracker.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentWeatherBinding
import com.elliottsoftware.calftracker.presentation.components.login.LoginView
import com.elliottsoftware.calftracker.presentation.components.weather.WeatherView
import com.elliottsoftware.calftracker.util.LocationManagerUtil
import com.elliottsoftware.calftracker.util.findActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@RequiresApi(Build.VERSION_CODES.O)
class WeatherFragment : Fragment() {
    private var _binding:FragmentWeatherBinding? = null;
    val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher. You can use either a val, as shown in this snippet,
// or a lateinit var in your onAttach() or onCreate() method.





    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherBinding.inflate(inflater,container,false)
        val view = binding.root


        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {



                WeatherView()

            }






        }


        return view
    }



    /**
     * Requests the [android.Manifest.permission.CAMERA] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)){

        }
    }
    private fun showCameraPreview() {
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview

        } else {
            // Permission is missing and must be requested.
            requestCameraPermission()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }


}