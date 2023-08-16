package com.rifqipadisiliwangi.sismartpju.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuerror.ResponsePjuItem
import com.rifqipadisiliwangi.sismartpju.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelPekerjaanPju :ViewModel() {

    var liveDataPju: MutableLiveData<ResponsePjuItem>
    var loading = MutableLiveData<Boolean>()

    init {
        liveDataPju = MutableLiveData()
        loading = MutableLiveData()
        callApiPju()

    }

    fun getPju():MutableLiveData<ResponsePjuItem>{
        return liveDataPju
    }

    fun callApiPju(){
        ApiClient.instance.getPju()
            .enqueue(object : Callback<ResponsePjuItem> {
                override fun onResponse(
                    call: Call<ResponsePjuItem>,
                    response: Response<ResponsePjuItem>
                ) {
                    if (response.isSuccessful){
                        liveDataPju.postValue(response.body())
                        Log.d("data",response.body()?.tipe.toString())
                    }else{
                        Log.d("data",response.body()?.tipe.toString())
                    }
                }

                override fun onFailure(call: Call<ResponsePjuItem>, t: Throwable) {
                    Log.d("data",call.toString())
                }

            })

    }


}