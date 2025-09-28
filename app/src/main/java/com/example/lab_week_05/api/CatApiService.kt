package com.example.lab_week_05.api
import com.example.lab_week_05.model.ImageData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApiService {
    @GET("images/search")
    fun searchImages(
        @Query("limit") limit: Int,
        @Query("size") format: String,
    ): Call<List<ImageData>>

    @GET("images/{id}")
    fun fetchImageById(
        @Path("id") imageId: String
    ): Call<ImageData>
}


