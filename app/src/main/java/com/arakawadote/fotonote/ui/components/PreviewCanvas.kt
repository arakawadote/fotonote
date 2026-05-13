package com.arakawadote.fotonote.ui.components

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
import com.arakawadote.fotonote.data.ImageDecoder
import com.arakawadote.fotonote.domain.model.ExifData
import com.arakawadote.fotonote.domain.model.FrameTemplate
import com.arakawadote.fotonote.util.cameraName
import com.arakawadote.fotonote.util.shootingSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PreviewCanvas(
    selectedImageUri: Uri?,
    exifData: ExifData?,
    selectedTemplate: FrameTemplate,
    isReadingExif: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val palette = remember(selectedTemplate) {
        selectedTemplate.previewPalette()
    }
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
            .background(palette.frameBackground)
            .border(1.dp, palette.borderColor)
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
                palette = palette,
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
    palette: PreviewPalette,
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
                    .border(1.dp, palette.photoBorderColor)
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
                        color = palette.secondaryText,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                settings.isNotEmpty() -> {
                    Text(
                        text = exifData.cameraName(),
                        color = palette.primaryText,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (settings.isNotEmpty()) {
                        Text(
                            text = settings,
                            color = palette.secondaryText,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    Text(
                        text = exifData.cameraName(),
                        color = palette.primaryText,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "EXIF情報が見つかりませんでした",
                        color = palette.secondaryText,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private data class PreviewPalette(
    val frameBackground: Color,
    val borderColor: Color,
    val photoBorderColor: Color,
    val primaryText: Color,
    val secondaryText: Color
)

private fun FrameTemplate.previewPalette(): PreviewPalette {
    return when (this) {
        FrameTemplate.ClassicWhite -> PreviewPalette(
            frameBackground = Color.White,
            borderColor = Color(0xFFDDDDDD),
            photoBorderColor = Color.Transparent,
            primaryText = Color(0xFF222222),
            secondaryText = Color(0xFF444444)
        )
        FrameTemplate.SoftGray -> PreviewPalette(
            frameBackground = Color(0xFFF1F2F2),
            borderColor = Color(0xFFD6D8D8),
            photoBorderColor = Color.White,
            primaryText = Color(0xFF1E2326),
            secondaryText = Color(0xFF586064)
        )
        FrameTemplate.Noir -> PreviewPalette(
            frameBackground = Color(0xFF111111),
            borderColor = Color(0xFF333333),
            photoBorderColor = Color(0xFF2C2C2C),
            primaryText = Color.White,
            secondaryText = Color(0xFFCFCFCF)
        )
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
