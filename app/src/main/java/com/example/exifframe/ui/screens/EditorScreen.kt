package com.example.exifframe.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exifframe.ui.EditorViewModel
import com.example.exifframe.ui.components.BannerAdView
import com.example.exifframe.ui.components.PreviewCanvas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = viewModel(),
    onOpenPrivacyPolicy: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                    // Some providers grant only a temporary read permission.
                }
            }
            viewModel.onImageSelected(uri)
        }
    )

    Scaffold(
        containerColor = Color(0xFFF2F2F2),
        bottomBar = {
            BannerAdView()
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FotoNote",
                        fontWeight = FontWeight.Medium
                    )
                },
                actions = {
                    TextButton(onClick = onOpenPrivacyPolicy) {
                        Text(
                            text = "Privacy",
                            color = Color(0xFF111111),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF111111)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            PreviewCanvas(
                selectedImageUri = state.selectedImageUri,
                exifData = state.exifData,
                isReadingExif = state.isReadingExif,
                modifier = Modifier.fillMaxWidth()
            )

            ControlPanel(
                hasSelectedImage = state.selectedImageUri != null,
                isReadingExif = state.isReadingExif,
                isExporting = state.isExporting,
                successMessage = state.successMessage,
                errorMessage = state.errorMessage,
                onPickImage = {
                    imagePicker.launch(arrayOf("image/*"))
                },
                onExportImage = viewModel::onExportRequested
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ControlPanel(
    hasSelectedImage: Boolean,
    isReadingExif: Boolean,
    isExporting: Boolean,
    successMessage: String?,
    errorMessage: String?,
    onPickImage: () -> Unit,
    onExportImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryButtonText = when {
        isReadingExif -> "読み取り中..."
        isExporting -> "書き出し中..."
        hasSelectedImage -> "画像を書き出す"
        else -> "画像を選択"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Classic White",
                    color = Color(0xFF111111),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "現在のテンプレート",
                    color = Color(0xFF444444),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (hasSelectedImage) {
                        onExportImage()
                    } else {
                        onPickImage()
                    }
                },
                enabled = !isReadingExif && !isExporting,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF111111),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(
                    text = primaryButtonText,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (hasSelectedImage) {
                OutlinedButton(
                    onClick = onPickImage,
                    enabled = !isReadingExif && !isExporting,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        text = "画像を変更",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        if (successMessage != null) {
            Text(
                text = successMessage,
                color = Color(0xFF1B5E20),
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFF8A1F11),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
