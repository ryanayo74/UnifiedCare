package com.ucb.unifiedcare.unifiedcare

import ModelClass.NominatimResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {
    @GET("search")
    fun geocodeAddress(
        @Query("q") address: String,
        @Query("format") format: String = "json"
    ):  Call<List<NominatimResult>?>
}