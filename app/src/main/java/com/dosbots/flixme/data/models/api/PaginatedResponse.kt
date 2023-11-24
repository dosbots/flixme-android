package com.dosbots.flixme.data.models.api

import com.squareup.moshi.Json

data class PaginatedResponse<T>(
    val page: Int,
    val results: List<T>,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "total_results") val totalResults: Int
)