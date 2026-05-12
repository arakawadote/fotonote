package com.arakawadote.fotonote.domain.model

data class ExifData(
    val make: String? = null,
    val model: String? = null,
    val lensModel: String? = null,
    val focalLength: String? = null,
    val fNumber: String? = null,
    val exposureTime: String? = null,
    val iso: String? = null,
    val dateTime: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
