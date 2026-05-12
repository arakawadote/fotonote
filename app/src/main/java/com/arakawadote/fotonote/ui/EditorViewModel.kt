package com.arakawadote.fotonote.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arakawadote.fotonote.data.ExifReader
import com.arakawadote.fotonote.domain.model.EditorState
import com.arakawadote.fotonote.domain.usecase.ExportImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val exifReader = ExifReader()
    private val exportImageUseCase = ExportImageUseCase()
    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    fun onImageSelected(uri: Uri?) {
        if (uri == null) return

        _state.update {
            it.copy(
                selectedImageUri = uri,
                exifData = null,
                isReadingExif = true,
                savedImageUri = null,
                successMessage = null,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    exifReader.read(getApplication<Application>(), uri)
                }
            }

            _state.update { state ->
                result.fold(
                    onSuccess = { exifData ->
                        state.copy(
                            exifData = exifData,
                            isReadingExif = false,
                            successMessage = null,
                            errorMessage = null
                        )
                    },
                    onFailure = {
                        state.copy(
                            exifData = null,
                            isReadingExif = false,
                            successMessage = null,
                            errorMessage = "EXIF情報を読み取れませんでした。"
                        )
                    }
                )
            }
        }
    }

    fun onExportRequested() {
        val imageUri = _state.value.selectedImageUri ?: return

        _state.update {
            it.copy(
                isExporting = true,
                successMessage = null,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    exportImageUseCase.export(
                        context = getApplication<Application>(),
                        imageUri = imageUri,
                        exifData = _state.value.exifData
                    )
                }
            }

            _state.update { state ->
                result.fold(
                    onSuccess = { savedUri ->
                        state.copy(
                            isExporting = false,
                            savedImageUri = savedUri,
                            successMessage = "画像を保存しました。",
                            errorMessage = null
                        )
                    },
                    onFailure = {
                        state.copy(
                            isExporting = false,
                            successMessage = null,
                            errorMessage = "画像の書き出しに失敗しました。"
                        )
                    }
                )
            }
        }
    }
}
