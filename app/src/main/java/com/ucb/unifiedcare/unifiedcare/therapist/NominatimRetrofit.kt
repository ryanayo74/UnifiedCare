package com.ucb.unifiedcare.unifiedcare.therapist

import com.ucb.unifiedcare.unifiedcare.NominatimApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NominatimRetrofit {
    private const val BASE_URL = "https://nominatim.openstreetmap.org/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val nominatimApi: NominatimApi = retrofit.create(NominatimApi::class.java)
}