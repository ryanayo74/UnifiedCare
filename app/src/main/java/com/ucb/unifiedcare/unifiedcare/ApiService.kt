package com.ucb.unifiedcare.unifiedcare

import ModelClass.Facility
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getFacilities(
        @Query("query") query: String
    ): Response<List<Facility>>
}
