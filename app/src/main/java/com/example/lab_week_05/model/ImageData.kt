package com.example.lab_week_05.model
import com.squareup.moshi.Json

data class ImageData(
    val id: String,
    @field:Json(name = "url") val imageUrl: String,
    val breeds: List<CatBreedData>?
) {
    val displayBreed: String
        get() = breeds?.firstOrNull()?.name ?: "Unknown"
}
