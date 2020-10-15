package com.taeho.maskappkotlin.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.taeho.maskappkotlin.model.Store
import com.taeho.maskappkotlin.repository.MaskService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel @ViewModelInject constructor(
    private val service: MaskService,
    private val  fusedLocationClient: FusedLocationProviderClient
): ViewModel() {
    val itemLiveData = MutableLiveData<List<Store>>()
    val loadingLiveData = MutableLiveData<Boolean>()



    init {
        fetchStoreInfo()

    }

    @SuppressLint("MissingPermission")
    fun fetchStoreInfo() {
        loadingLiveData.value = true


        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            viewModelScope.launch {
                val storeInfo = service.fetchStoreInfo(location.latitude, location.longitude)
                itemLiveData.value = storeInfo.stores.filter {store ->  
                    store.remain_stat != null
                }

                loadingLiveData.value = false
            }
        }.addOnFailureListener {exception ->
            loadingLiveData.value = false
        }

    }
}