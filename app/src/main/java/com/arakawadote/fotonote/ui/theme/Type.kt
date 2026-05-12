package com.arakawadote.fotonote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList

private val JapaneseLocaleList = LocaleList(Locale("ja-JP"))

private fun TextStyle.withJapaneseGlyphs(): TextStyle {
    return copy(
        fontFamily = FontFamily.SansSerif,
        localeList = JapaneseLocaleList
    )
}

private val DefaultTypography = Typography()

val Typography = DefaultTypography.copy(
    displayLarge = DefaultTypography.displayLarge.withJapaneseGlyphs(),
    displayMedium = DefaultTypography.displayMedium.withJapaneseGlyphs(),
    displaySmall = DefaultTypography.displaySmall.withJapaneseGlyphs(),
    headlineLarge = DefaultTypography.headlineLarge.withJapaneseGlyphs(),
    headlineMedium = DefaultTypography.headlineMedium.withJapaneseGlyphs(),
    headlineSmall = DefaultTypography.headlineSmall.withJapaneseGlyphs(),
    titleLarge = DefaultTypography.titleLarge.withJapaneseGlyphs(),
    titleMedium = DefaultTypography.titleMedium.withJapaneseGlyphs(),
    titleSmall = DefaultTypography.titleSmall.withJapaneseGlyphs(),
    bodyLarge = DefaultTypography.bodyLarge.withJapaneseGlyphs(),
    bodyMedium = DefaultTypography.bodyMedium.withJapaneseGlyphs(),
    bodySmall = DefaultTypography.bodySmall.withJapaneseGlyphs(),
    labelLarge = DefaultTypography.labelLarge.withJapaneseGlyphs(),
    labelMedium = DefaultTypography.labelMedium.withJapaneseGlyphs(),
    labelSmall = DefaultTypography.labelSmall.withJapaneseGlyphs()
)
