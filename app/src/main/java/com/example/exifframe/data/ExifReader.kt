package com.example.exifframe.data

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.example.exifframe.domain.model.ExifData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

class ExifReader {
    fun read(context: Context, uri: Uri): ExifData {
        val resolver = context.contentResolver
        resolver.openInputStream(uri)?.use { input ->
            val exif = ExifInterface(input)
            return ExifData(
                make = exif.cleanAttribute(ExifInterface.TAG_MAKE),
                model = exif.cleanAttribute(ExifInterface.TAG_MODEL),
                lensModel = exif.cleanAttribute(ExifInterface.TAG_LENS_MODEL),
                focalLength = exif.formatFocalLength(),
                fNumber = exif.formatFNumber(),
                exposureTime = exif.formatExposureTime(),
                iso = exif.cleanAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
                    ?: exif.cleanAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS),
                dateTime = exif.formatDateTime(),
                latitude = exif.latLong?.getOrNull(0),
                longitude = exif.latLong?.getOrNull(1)
            )
        }

        return ExifData()
    }
}

private fun ExifInterface.cleanAttribute(tag: String): String? {
    return getAttribute(tag)
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
}

private fun ExifInterface.formatFocalLength(): String? {
    val value = getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH, Double.NaN)
    if (value.isNaN() || value <= 0.0) return null

    return "${formatNumber(value)}mm"
}

private fun ExifInterface.formatFNumber(): String? {
    val value = getAttributeDouble(ExifInterface.TAG_F_NUMBER, Double.NaN)
    if (value.isNaN() || value <= 0.0) return null

    return "f/${formatNumber(value)}"
}

private fun ExifInterface.formatExposureTime(): String? {
    val seconds = getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, Double.NaN)
    if (seconds.isNaN() || seconds <= 0.0) return null

    if (seconds >= 1.0) {
        return "${formatNumber(seconds)}s"
    }

    val reciprocal = 1.0 / seconds
    val rounded = reciprocal.roundToInt()
    if (abs(reciprocal - rounded) < 0.1) {
        return "1/${rounded}s"
    }

    return String.format(Locale.US, "%.3fs", seconds).trimEnd('0').trimEnd('.')
}

private fun ExifInterface.formatDateTime(): String? {
    val raw = cleanAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
        ?: cleanAttribute(ExifInterface.TAG_DATETIME)
        ?: return null

    return runCatching {
        val parsed = LocalDateTime.parse(
            raw,
            DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss", Locale.US)
        )
        parsed.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.US))
    }.getOrElse {
        raw
    }
}

private fun formatNumber(value: Double): String {
    val rounded = value.roundToInt()
    if (abs(value - rounded) < 0.05) {
        return rounded.toString()
    }

    return String.format(Locale.US, "%.1f", value).trimEnd('0').trimEnd('.')
}
