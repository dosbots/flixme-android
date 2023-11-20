package com.dosbots.flixme.data.models.api

import com.squareup.moshi.Json

data class PaginatedResponse<T>(
    val page: Int,
    val results: List<T>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)