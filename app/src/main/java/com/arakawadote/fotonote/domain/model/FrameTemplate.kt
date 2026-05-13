package com.arakawadote.fotonote.domain.model

enum class FrameTemplate(
    val title: String,
    val shortLabel: String,
    val description: String
) {
    ClassicWhite(
        title = "Classic White",
        shortLabel = "White",
        description = "余白を活かした標準の白フレーム"
    ),
    SoftGray(
        title = "Soft Gray",
        shortLabel = "Gray",
        description = "やわらかなグレー背景の落ち着いたフレーム"
    ),
    Noir(
        title = "Noir",
        shortLabel = "Noir",
        description = "黒背景で写真と撮影情報を引き締めるフレーム"
    )
}
