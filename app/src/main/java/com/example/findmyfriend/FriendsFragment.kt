package com.example.findmyfriend

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmyfriend.Adapter.UserAdapter
import com.example.findmyfriend.View.MapsActivity
import com.example.findmyfriend.Viewmodel.AuthenticationViewModel
import com.example.findmyfriend.Viewmodel.FirestoreViewModel
import com.example.findmyfriend.Viewmodel.LocationViewModel
import com.example.findmyfriend.databinding.FragmentFriendsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class FriendsFragment : Fragment() {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentFriendsBinding.inflate(inflater,container, false)

        firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        authenticationViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationViewModel.initializeFusedLocationClient(fusedLocationClient)

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission is already granted
            getLocation()
        }
        userAdapter = UserAdapter(emptyList())
        binding.userRV.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        fetchUsers()

        binding.locationBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }


        return binding.root
    }

    private fun fetchUsers() {
        firestoreViewModel.getAllUsers(requireContext()){
            userAdapter.updateData(it)
        }
    }

    private fun getLocation() {
        locationViewModel.getLastLocation {
            // Save location to Firestore for the current user
            authenticationViewModel.getCurrentUserId().let { userId ->
                firestoreViewModel.updateUserLocation(requireContext(),userId, it)
            }
        }
    }

}