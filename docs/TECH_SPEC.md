# 技術仕様書：FotoNote

## 1. 対象プラットフォーム

Androidネイティブアプリ。

---

## 2. 使用言語

Kotlin。

---

## 3. UIフレームワーク

Jetpack Compose。

---

## 4. 最小対応Androidバージョン

推奨は以下。

```txt
Android 8.0 / API 26
```

必要に応じて調整可能。

---

## 5. 主な使用ライブラリ

## 5.1 EXIF読み取り

AndroidX ExifInterfaceを使用する。

### 目的

- 選択画像からEXIF情報を読み取る。
- 画像の向きを取得する。
- カメラ情報を抽出する。
- 撮影設定を抽出する。

---

## 5.2 広告

Google Mobile Ads SDKを使用する。

### 広告形式

- AdMobバナー広告
- 画面下固定
- Anchored Adaptive Bannerを想定

---

## 5.3 画像選択

Activity Result APIのOpenDocumentを使用する。

---

## 5.4 画像保存

MediaStoreを使用して、書き出した画像を端末ギャラリーに保存する。

---

## 6. アーキテクチャ

シンプルなMVVM構成とする。

```txt
ui/
  MainActivity.kt
  screens/
    EditorScreen.kt
  components/
    PreviewCanvas.kt
    MetadataPanel.kt
    BannerAdView.kt

domain/
  model/
    ExifData.kt
    FrameTemplate.kt
    ExportSize.kt
  usecase/
    ReadExifUseCase.kt
    ExportImageUseCase.kt

data/
  ExifReader.kt
  ImageSaver.kt

ads/
  AdMobBanner.kt

util/
  Formatters.kt
```

---

## 7. データモデル

## 7.1 ExifData

```kotlin
data class ExifData(
    val make: String? = null,
    val model: String? = null,
    val lensModel: String? = null,
    val focalLength: String? = null,
    val fNumber: String? = null,
    val exposureTime: String? = null,
    val iso: String? = null,
    val dateTime: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
```

---

## 7.2 EditorState

```kotlin
data class EditorState(
    val selectedImageUri: Uri? = null,
    val exifData: ExifData? = null,
    val selectedTemplate: FrameTemplate = FrameTemplate.ClassicWhite,
    val exportWidth: Int = 1080,
    val isExporting: Boolean = false,
    val errorMessage: String? = null
)
```

---

## 7.3 FrameTemplate

```kotlin
enum class FrameTemplate {
    ClassicWhite
}
```

---

## 7.4 ExportSize

```kotlin
const val EXPORT_WIDTH = 1080
```

---

## 8. EXIF読み取り要件

以下のEXIFタグを読み取る。

```txt
TAG_MAKE
TAG_MODEL
TAG_LENS_MODEL
TAG_FOCAL_LENGTH
TAG_F_NUMBER
TAG_EXPOSURE_TIME
TAG_PHOTOGRAPHIC_SENSITIVITY
TAG_DATETIME_ORIGINAL
TAG_GPS_LATITUDE
TAG_GPS_LONGITUDE
```

GPS情報は読み取っても、MVPでは表示しない。

---

## 9. メタデータ整形仕様

## 9.1 カメラ名

メーカー名と機種名が存在する場合。

```txt
{make} {model}
```

機種名のみ存在する場合。

```txt
{model}
```

どちらも存在しない場合。

```txt
Unknown Camera
```

---

## 9.2 撮影設定

### 表示形式

```txt
{focalLength}  {fNumber}  {exposureTime}  ISO{iso}
```

### 表示例

```txt
24mm  f/1.8  1/250s  ISO100
```

### 欠損値の扱い

存在しない値はスキップする。

例。

```txt
f/1.8  1/250s  ISO100
```

---

## 9.3 撮影日時

### 表示形式

```txt
yyyy.MM.dd HH:mm
```

### 表示例

```txt
2026.05.10 19:47
```

---

## 10. 書き出し仕様

完成画像はBitmapとして生成する。

### 要件

- 幅1080pxで生成し、高さは写真比率とメタデータ表示量に合わせて可変にする。
- 背景を白で描画する。
- 写真を指定エリアに描画する。
- 写真下部にメタデータテキストを描画する。
- UIボタンは描画しない。
- 広告は描画しない。

### 推奨実装

書き出しはCompose画面のキャプチャではなく、Android Canvasで個別にBitmapを生成する。

理由。

- 書き出しサイズを正確に制御しやすい。
- 広告やUI部品を除外しやすい。
- プレビュー表示と書き出し処理を分離できる。
- 将来的なテンプレート追加に対応しやすい。

---

## 11. 保存仕様

書き出し完了後、画像を端末ギャラリーに保存する。

### 保存形式

```txt
JPEG
```

### 品質

```txt
95
```

### ファイル名

```txt
FOTONOTE_yyyyMMdd_HHmmss.jpg
```

---

## 12. 広告仕様

AdMobバナー広告を使用する。

### 要件

- エディタ画面下部に表示する。
- 開発中はテスト広告ユニットIDを使用する。
- 本番リリース時に本番広告ユニットIDへ差し替える。
- 広告読み込みに失敗してもアプリがクラッシュしないようにする。
- 広告は書き出し画像に含めない。
- インタースティシャル広告はMVPでは使用しない。
- リワード広告はMVPでは使用しない。

---

## 13. 権限

可能な限り広範囲なストレージ権限は使用しない。

### 方針

- 画像選択はOpenDocumentを使用する。
- 書き出し画像の保存はMediaStoreを使用する。
- 端末内の全画像アクセス権限は原則不要とする。

---

## 14. プライバシー

- 画像は端末内で処理する。
- 画像をサーバーに送信しない。
- EXIF情報をサーバーに送信しない。
- GPS情報は初期状態では表示しない。
- 個人情報を収集しない。
- アカウント作成機能はMVPでは実装しない。

---

## 15. テスト項目

## 15.1 画像選択

- JPEG画像を選択できる。
- PNG画像を選択できる。
- 画像選択をキャンセルしてもクラッシュしない。
- 大きな画像を選択してもクラッシュしない。

---

## 15.2 EXIF

- カメラメーカー名を表示できる。
- カメラ機種名を表示できる。
- ISOを表示できる。
- F値を表示できる。
- シャッタースピードを表示できる。
- 撮影日時を表示できる。
- EXIFがない画像でもクラッシュしない。
- GPS情報が表示されない。

---

## 15.3 プレビュー

- 選択画像がプレビューに表示される。
- 縦画像でも崩れない。
- 横画像でも崩れない。
- メタデータ表示が読みやすい。
- 画面サイズが異なっても表示が崩れない。

---

## 15.4 書き出し

- 指定サイズで画像を書き出せる。
- 書き出し画像に広告が含まれない。
- 書き出し画像にUIボタンが含まれない。
- 書き出し画像がギャラリーに保存される。
- 文字が読みやすい状態で保存される。

---

## 15.5 広告

- テスト広告が表示される。
- 画面下に固定される。
- 操作ボタンと重ならない。
- 広告読み込み失敗時もレイアウトが崩れない。
