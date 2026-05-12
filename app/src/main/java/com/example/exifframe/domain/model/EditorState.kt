package com.example.exifframe.domain.model

import android.net.Uri

data class EditorState(
    val selectedImageUri: Uri? = null,
    val exifData: ExifData? = null,
    val isReadingExif: Boolean = false,
    val isExporting: Boolean = false,
    val savedImageUri: Uri? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
