package dev.robert.foodonor.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.foodonor.R
import dev.robert.foodonor.databinding.FragmentDonateBinding
import dev.robert.foodonor.model.Donation
import dev.robert.foodonor.utils.Resource
import dev.robert.foodonor.viewmodel.DonationsViewModel
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class DonateFragment : Fragment(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private lateinit var binding: FragmentDonateBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_CODE = 10
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var auth: FirebaseAuth
    private lateinit var userID: String
    private lateinit var database: FirebaseFirestore

    private val viewModel: DonationsViewModel by viewModels()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDonateBinding.inflate(inflater, container, false)
        val view = binding.root

        (activity as AppCompatActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        userID = auth.currentUser!!.uid


        mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment.getMapAsync(this)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }

        return view
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient.connect()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        buildGoogleApiClient()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(p0: Location) {
        mLastLocation = p0
        val latLng = LatLng(p0.latitude, p0.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("You are here")

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        mMap.addMarker(markerOptions)!!.showInfoWindow()

        binding.submit.setOnClickListener {
            val donorName = binding.nameError.editText?.text.toString()
            val foodItem = binding.itemError.editText?.text.toString()
            val phoneNumber = binding.phoneError.editText?.text.toString()
            val address = binding.decriptionError.editText?.text.toString()
            val donationUniqueId = UUID.randomUUID().toString()
            when {
                donorName.isEmpty() -> {
                    binding.nameError.isErrorEnabled = true
                    binding.nameError.error = "Please enter your name"
                }
                foodItem.isEmpty() -> {
                    binding.itemError.isErrorEnabled = true
                    binding.itemError.error = "Please enter your food item"
                }
                phoneNumber.isEmpty() -> {
                    binding.phoneError.isErrorEnabled = true
                    binding.phoneError.error = "Please enter your phone number"
                }
                address.isEmpty() -> {
                    binding.decriptionError.isErrorEnabled = true
                    binding.decriptionError.error = "Please enter your address"
                }
                else -> {
                    val geoPoint = GeoPoint(p0.latitude, p0.longitude)
                    binding.nameError.isErrorEnabled = false
                    binding.itemError.isErrorEnabled = false
                    binding.phoneError.isErrorEnabled = false
                    binding.decriptionError.isErrorEnabled = false
                    // val serverTimestamp = FirebaseDatabase.getInstance().app.options.
                    val donation = Donation(
                        userID,
                        donorName,
                        foodItem,
                        phoneNumber,
                        address,
                        geoPoint,
                        false,
                        donationUniqueId
                    )
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.donate(donation)
                    }

                    viewModel.donate.observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Loading -> {
                                binding.submit.isEnabled = false
                                binding.submit.text = "Loading..."
                                binding.progressCircular.isVisible = true
                            }
                            is Resource.Success -> {
                                binding.submit.isEnabled = true
                                binding.submit.text = "Submit"
                                binding.progressCircular.isVisible = false
                                Toast.makeText(
                                    requireContext(),
                                    "Donation Successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                requireActivity().onBackPressed()
                            }
                            is Resource.Error -> {
                                binding.submit.isEnabled = true
                                binding.submit.text = "Submit"
                                binding.progressCircular.isVisible = false
                                Toast.makeText(requireContext(), it.string, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mapFragment.getMapAsync(this)
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}