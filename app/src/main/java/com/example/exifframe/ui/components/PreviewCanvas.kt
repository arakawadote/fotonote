package com.example.exifframe.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.exifframe.data.ImageDecoder
import com.example.exifframe.domain.model.ExifData
import com.example.exifframe.util.cameraName
import com.example.exifframe.util.shootingSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PreviewCanvas(
    selectedImageUri: Uri?,
    exifData: ExifData?,
    isReadingExif: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, selectedImageUri) {
        value = null
        selectedImageUri ?: return@produceState
        value = withContext(Dispatchers.IO) {
            loadPreviewBitmap(context, selectedImageUri)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color(0xFFDDDDDD))
            .padding(14.dp)
    ) {
        if (bitmap == null) {
            EmptyPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
            )
        } else {
            FramedPhotoPreview(
                bitmap = bitmap,
                exifData = exifData,
                isReadingExif = isReadingExif,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun FramedPhotoPreview(
    bitmap: Bitmap?,
    exifData: ExifData?,
    isReadingExif: Boolean,
    modifier: Modifier = Modifier
) {
    val settings = exifData.shootingSettings()
    val photoAspectRatio = remember(bitmap) {
        val width = bitmap?.width ?: 4
        val height = bitmap?.height ?: 3
        (width.toFloat() / height.toFloat()).coerceIn(0.55f, 2.2f)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "選択した写真",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(photoAspectRatio),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when {
                isReadingExif -> {
                    Text(
                        text = "EXIF読み取り中",
                        color = Color(0xFF444444),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                settings.isNotEmpty() -> {
                    Text(
                        text = exifData.cameraName(),
                        color = Color(0xFF222222),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (settings.isNotEmpty()) {
                        Text(
                            text = settings,
                            color = Color(0xFF444444),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    Text(
                        text = exifData.cameraName(),
                        color = Color(0xFF222222),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "EXIF情報が見つかりませんでした",
                        color = Color(0xFF444444),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyPreview(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(Color(0xFFF2F2F2)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PM",
                color = Color(0xFF111111),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "写真を選択してください",
            color = Color(0xFF111111),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "お気に入りの1枚から始めましょう。",
            color = Color(0xFF444444),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

private fun loadPreviewBitmap(
    context: Context,
    uri: Uri,
    maxDimension: Int = 2400
): Bitmap? {
    return ImageDecoder.decodeOrientedBitmap(
        context = context,
        uri = uri,
        maxDimension = maxDimension
    )
}
