package com.elliottsoftware.calftracker.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.*
//import androidx.compose.material.AppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.Navigation
import com.elliottsoftware.calftracker.R
import com.elliottsoftware.calftracker.databinding.FragmentMainBinding
import com.elliottsoftware.calftracker.presentation.components.main.AppBar
import com.elliottsoftware.calftracker.presentation.components.main.DrawerBody
import com.elliottsoftware.calftracker.presentation.components.main.DrawerHeader
import kotlinx.coroutines.launch



/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.composeView.apply{
            setContent {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                Scaffold(

                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Calf Tracker")
                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary,
                            navigationIcon = {
                                IconButton(onClick = {}) {
                                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle navigation drawer" )
                                }
                            }
                        )
                    }
                ) {}
            }

        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}