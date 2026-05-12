package com.arakawadote.fotonote.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import com.arakawadote.fotonote.data.ImageDecoder
import com.arakawadote.fotonote.data.ImageSaver
import com.arakawadote.fotonote.domain.model.ExifData
import com.arakawadote.fotonote.util.cameraName
import com.arakawadote.fotonote.util.shootingSettings
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class ExportImageUseCase(
    private val imageSaver: ImageSaver = ImageSaver()
) {
    fun export(
        context: Context,
        imageUri: Uri,
        exifData: ExifData?
    ): Uri {
        val sourceBitmap = ImageDecoder.decodeOrientedBitmap(
            context = context,
            uri = imageUri,
            maxDimension = 4200
        ) ?: error("画像を読み込めませんでした。")

        val frameBitmap = createFrameBitmap(
            sourceBitmap = sourceBitmap,
            exifData = exifData
        )

        return imageSaver.saveJpeg(
            context = context,
            bitmap = frameBitmap,
            displayName = createFileName()
        )
    }

    private fun createFrameBitmap(
        sourceBitmap: Bitmap,
        exifData: ExifData?
    ): Bitmap {
        val outputWidth = 1080
        val horizontalPadding = 48f
        val topPadding = 48f
        val photoWidth = outputWidth - horizontalPadding * 2
        val photoAspect = sourceBitmap.width.toFloat() / sourceBitmap.height.toFloat()
        val photoHeight = photoWidth / photoAspect

        val metadataTopSpacing = 70f
        val metadataBottomPadding = 58f
        val cameraTextSize = 28f
        val settingsTextSize = 23f
        val settings = exifData.shootingSettings()
        val lineGap = 38f
        val metadataTextHeight = if (settings.isNotEmpty()) lineGap else 0f
        val metadataBlockHeight = 86f
        val metadataVisualOffset = -8f
        val outputHeight = (
            topPadding +
                photoHeight +
                metadataTopSpacing +
                metadataBlockHeight +
                metadataBottomPadding
            ).roundToInt()

        val outputBitmap = Bitmap.createBitmap(
            outputWidth,
            outputHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(outputBitmap)
        canvas.drawColor(Color.WHITE)

        val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val imageRect = RectF(
            horizontalPadding,
            topPadding,
            horizontalPadding + photoWidth,
            topPadding + photoHeight
        )
        canvas.drawBitmap(sourceBitmap, null, imageRect, imagePaint)

        val centerX = outputWidth / 2f
        val cameraPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(34, 34, 34)
            textAlign = Paint.Align.CENTER
            textSize = cameraTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val settingsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(68, 68, 68)
            textAlign = Paint.Align.CENTER
            textSize = settingsTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        var baseline = imageRect.bottom +
            metadataTopSpacing +
            ((metadataBlockHeight - metadataTextHeight) / 2f) +
            metadataVisualOffset
        canvas.drawText(exifData.cameraName(), centerX, baseline, cameraPaint)

        if (settings.isNotEmpty()) {
            baseline += lineGap
            canvas.drawText(settings, centerX, baseline, settingsPaint)
        }

        return outputBitmap
    }

    private fun createFileName(): String {
        val timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss", Locale.US)
        )
        return "FOTONOTE_$timestamp.jpg"
    }
}
