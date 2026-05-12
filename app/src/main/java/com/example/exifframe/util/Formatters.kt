package com.example.exifframe.util

import com.example.exifframe.domain.model.ExifData

fun ExifData?.cameraName(): String {
    val make = this?.make.clean()
    val model = this?.model.clean()

    return when {
        make != null && model != null -> "$make $model"
        model != null -> model
        make != null -> make
        else -> "Unknown Camera"
    }
}

fun ExifData?.shootingSettings(): String {
    val parts = listOfNotNull(
        this?.focalLength.clean(),
        this?.fNumber.clean(),
        this?.exposureTime.clean(),
        this?.iso.clean()?.let { "ISO$it" }
    )

    return parts.joinToString("  ")
}

fun ExifData?.shootingDateTime(): String {
    return this?.dateTime.clean().orEmpty()
}

private fun String?.clean(): String? {
    return this
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
}
