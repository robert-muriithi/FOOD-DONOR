package dev.robert.foodonor.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.foodonor.model.Donation
import dev.robert.foodonor.repository.MainRepository
import dev.robert.foodonor.utils.Resource
import javax.inject.Inject

@HiltViewModel
class DonationsViewModel @Inject constructor(
    private val repository: MainRepository,
    private val database: FirebaseFirestore
) : ViewModel() {
    private  val TAG = "DonationsViewModel"
    private val _donations = MutableLiveData<Resource<List<Donation>>>()
    val donations : LiveData<Resource<List<Donation>>> = _donations

    suspend fun getDonations()  {
        _donations.value = Resource.Loading
        repository.getDonations { donations->
            _donations.value = donations
        }
    }

    private val _donate = MutableLiveData<Resource<List<Donation>>>()
    val donate : LiveData<Resource<List<Donation>>> = _donate

    suspend fun donate(donation : Donation){
        _donate.value = Resource.Loading
        repository.donate(donation){ donate ->
            _donate.value = donate
        }
    }

    private val _history = MutableLiveData<Resource<List<Donation>>>()
    val history : LiveData<Resource<List<Donation>>> = _history

    suspend fun getHistory() {
        _history.value = Resource.Loading
            repository.fetchHistory { history ->
            _history.value = history
        }
    }

    /*private val _listener = MutableLiveData<Resource<List<Donation>>>()
    val listener : LiveData<Resource<List<Donation>>> = _listener

     fun listenToChanges(){
        _listener.value = Resource.Loading
        database.collection("Donations").addSnapshotListener { value, error ->
            if (error != null){
                Log.d(TAG, "listenToChanges: ${error.message}")
            }
            if (value != null){
                val donations = ArrayList<Donation>()
                val documents = value.documents
                documents.forEach {
                    val donation = it.toObject(Donation::class.java)
                    if (donation != null){
                        donations.add(donation)
                    }
                }
                _listener.value = Resource.Success(donations)
            }
        }
    }*/
}