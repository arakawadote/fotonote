package com.arakawadote.fotonote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.MobileAds
import com.arakawadote.fotonote.ui.screens.EditorScreen
import com.arakawadote.fotonote.ui.screens.PrivacyPolicyScreen
import com.arakawadote.fotonote.ui.theme.FotoNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            MobileAds.initialize(this) {}
        }.start()

        setContent {
            FotoNoteTheme {
                var showPrivacyPolicy by remember { mutableStateOf(false) }

                if (showPrivacyPolicy) {
                    PrivacyPolicyScreen(
                        onBack = {
                            showPrivacyPolicy = false
                        }
                    )
                } else {
                    EditorScreen(
                        onOpenPrivacyPolicy = {
                            showPrivacyPolicy = true
                        }
                    )
                }
            }
        }
    }
}
