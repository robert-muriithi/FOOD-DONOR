package dev.robert.foodonor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.foodonor.model.Donation
import dev.robert.foodonor.repository.MainRepository
import dev.robert.foodonor.utils.Resource
import javax.inject.Inject

@HiltViewModel
class DonationsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _donations = MutableLiveData<Resource<List<Donation>>>()
    val liveData : LiveData<Resource<List<Donation>>> = _donations

    suspend fun getDonations()  {
        _donations.value = Resource.Loading
        repository.getDonations { items->
            _donations.value = items
        }
    }
}