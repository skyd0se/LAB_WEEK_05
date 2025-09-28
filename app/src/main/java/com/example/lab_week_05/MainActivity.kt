package com.example.lab_week_05

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }
    private val imageResultView: ImageView by lazy {
        findViewById(R.id.image_result)
    }
    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(1, "full")
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(TAG, "Failed to get search response", t)
            }

            override fun onResponse(
                call: Call<List<ImageData>>,
                response: Response<List<ImageData>>
            ) {
                if (response.isSuccessful) {
                    val firstImage = response.body()?.firstOrNull()
                    val imageId = firstImage?.id

                    if (!imageId.isNullOrBlank()) {
                        fetchImageDetails(imageId)
                    } else {
                        apiResponseView.text = "No image found"
                    }
                } else {
                    Log.e(TAG, "Search failed: ${response.errorBody()?.string()}")
                }
            }
        })
    }

    private fun fetchImageDetails(imageId: String) {
        val call = catApiService.fetchImageById(imageId)
        call.enqueue(object : Callback<ImageData> {
            override fun onFailure(call: Call<ImageData>, t: Throwable) {
                Log.e(TAG, "Failed to fetch image details", t)
            }

            override fun onResponse(call: Call<ImageData>, response: Response<ImageData>) {
                if (response.isSuccessful) {
                    val imageData = response.body()

                    if (imageData != null) {
                        imageLoader.loadImage(imageData.imageUrl, imageResultView)
                        apiResponseView.text = "Breed Name : ${imageData.displayBreed}"
                    }
                } else {
                    Log.e(TAG, "Details fetch failed: ${response.errorBody()?.string()}")
                }
            }
        })
    }
}
