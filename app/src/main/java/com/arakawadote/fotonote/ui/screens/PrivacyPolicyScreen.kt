package com.arakawadote.fotonote.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF2F2F2),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "プライバシーポリシー",
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(
                            text = "←",
                            color = Color(0xFF111111),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier.width(48.dp))
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
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .background(Color.White)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "FotoNote",
                color = Color(0xFF111111),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "最終更新日: 2026年5月12日",
                color = Color(0xFF444444),
                style = MaterialTheme.typography.bodySmall
            )

            PolicySection(
                title = "取り扱う情報",
                body = "本アプリは、ユーザーが選択した画像と、その画像に含まれるEXIF情報を端末内で読み取ります。カメラメーカー名、機種名、焦点距離、F値、シャッタースピード、ISO感度などをフレーム画像の作成に利用します。"
            )

            PolicySection(
                title = "利用目的",
                body = "選択された画像とEXIF情報は、アプリ画面でのプレビュー表示、カメラ情報付きフレーム画像の生成、生成画像の端末ギャラリーへの保存のために利用します。"
            )

            PolicySection(
                title = "外部送信",
                body = "現在のMVPでは、画像およびEXIF情報を外部サーバーへ送信しません。画像処理とEXIF読み取りは端末内で完結します。"
            )

            PolicySection(
                title = "GPS情報",
                body = "画像にGPS情報が含まれる場合でも、現在のMVPでは画面および書き出し画像には表示しません。"
            )

            PolicySection(
                title = "広告について",
                body = "現在の内部テスト版では、Google Mobile Ads SDKを使用してテスト広告を表示します。本番公開時にAdMobを利用する場合は、広告配信に関する情報をこのポリシーとGoogle PlayのData safetyに反映します。"
            )

            PolicySection(
                title = "保存と削除",
                body = "ユーザーが書き出し操作を行った場合にのみ、生成した画像を端末のギャラリーへ保存します。保存された画像は、端末のギャラリーアプリまたはファイル管理アプリから削除できます。"
            )

            PolicySection(
                title = "お問い合わせ",
                body = "本プライバシーポリシーに関するお問い合わせは、developmentbyarakawadote@gmail.com までお願いします。"
            )
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            color = Color(0xFF111111),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = body,
            color = Color(0xFF444444),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
