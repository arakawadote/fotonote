package com.arakawadote.fotonote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arakawadote.fotonote.ads.AdMobConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        val adWidth = with(density) {
            maxWidth.toPx().toInt()
        }
        val adWidthDp = with(density) {
            adWidth.toDp().value.toInt()
        }.coerceAtLeast(320)
        val adSize = remember(adWidthDp) {
            AdSize.getLargeAnchoredAdaptiveBannerAdSize(context, adWidthDp)
        }
        val adView = remember(adSize) {
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = AdMobConfig.BANNER_AD_UNIT_ID
                loadAd(AdRequest.Builder().build())
            }
        }

        DisposableEffect(adView) {
            onDispose {
                adView.destroy()
            }
        }

        AndroidView(
            factory = { adView },
            modifier = Modifier
                .fillMaxWidth()
                .height(adSize.height.dp)
        )
    }
}
