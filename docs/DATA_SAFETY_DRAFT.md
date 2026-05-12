# Google Play Data safety回答案：FotoNote

最終更新日：2026-05-12

この文書は、Google Play ConsoleのData safety入力用メモです。最終送信前にPlay Console画面の最新設問と照合してください。

## 公式参照

- Google Play Data safety form
  - https://support.google.com/googleplay/android-developer/answer/10787469
- Google Mobile Ads SDKのData safety開示情報
  - https://developers.google.com/admob/android/privacy/play-data-disclosure

## 前提

- FotoNote本体は、ユーザーが選択した写真とEXIF情報を端末内で処理します。
- 写真、EXIF情報、生成画像をアプリ開発者の外部サーバーへ送信しません。
- Google Playの説明では、端末内のみで処理され外部送信されないデータはData safety上の「収集」として申告不要です。
- ただし、アプリにはGoogle Mobile Ads SDKが含まれます。
- Google Mobile Ads SDKは、IPアドレス、ユーザー操作、診断情報、デバイス/アカウント識別子を広告、分析、不正防止目的で自動的に収集・共有すると公式に説明されています。

## Internal testingだけの場合

Google Playヘルプでは、内部テストトラックのみで有効なアプリはData safetyセクションへの掲載対象外とされています。

ただし、後でクローズドテストや本公開へ進むため、以下の回答案を準備しておきます。

## 全体回答案

### データの収集または共有

```txt
Yes
```

理由：FotoNote本体は写真/EXIFを外部送信しませんが、Google Mobile Ads SDKにより一部データの収集・共有が発生します。

### すべてのユーザーデータは転送中に暗号化されますか

```txt
Yes
```

理由：Google Mobile Ads SDK公式情報で、SDKが収集するユーザーデータはTLSで暗号化されると説明されています。

### ユーザーがデータ削除をリクエストする方法を提供していますか

```txt
Yes
```

理由：問い合わせ先メールアドレス `developmentbyarakawadote@gmail.com` を公開しています。アプリ開発者側で写真/EXIF/生成画像をサーバー保存しないため、端末内に保存された生成画像はユーザー自身がギャラリー等から削除できます。広告IDはAndroidの設定からリセットまたは削除できます。

## 選択するデータタイプ案

### 1. Location / Approximate location

```txt
Collected: Yes
Shared: Yes
Purposes: Advertising or marketing, Analytics, Fraud prevention, security, and compliance
Required/Optional: Required
```

理由：Google Mobile Ads SDKはIPアドレスを収集し、端末のおおまかな位置推定に使われる可能性があると説明されています。

### 2. App activity / App interactions

```txt
Collected: Yes
Shared: Yes
Purposes: Advertising or marketing, Analytics, Fraud prevention, security, and compliance
Required/Optional: Required
```

理由：Google Mobile Ads SDKはアプリ起動、タップ、動画表示などのユーザー操作情報を収集すると説明されています。

### 3. App info and performance / Diagnostics

```txt
Collected: Yes
Shared: Yes
Purposes: Advertising or marketing, Analytics, Fraud prevention, security, and compliance
Required/Optional: Required
```

理由：Google Mobile Ads SDKはアプリやSDKのパフォーマンス、起動時間、ハング率、電力使用などの診断情報を収集すると説明されています。

### 4. Device or other IDs

```txt
Collected: Yes
Shared: Yes
Purposes: Advertising or marketing, Analytics, Fraud prevention, security, and compliance
Required/Optional: Required
```

理由：Google Mobile Ads SDKはAndroid広告ID、App Set ID、必要に応じて端末上のログイン済みアカウント関連識別子を収集すると説明されています。

## 選択しないデータタイプ案

以下は、現在のMVPでは外部送信しないためData safety上は選択しない想定です。

- Photos and videos / Photos
- Files and docs
- Personal info / Email address
- Contacts
- Messages
- Financial info
- Health and fitness
- Calendar
- Audio files
- Web browsing history

## Play Consoleのその他設定メモ

```txt
広告を含む: Yes
アプリ内購入: No
アプリカテゴリ: 写真
対象年齢: 子ども向けではない
ログイン/アカウント作成: No
```

## 注意

- 本番AdMob IDへ差し替えた後も、この回答案を再確認してください。
- AdMob以外のSDKを追加した場合は、Data safety回答を必ず更新してください。
- Data safetyの申告内容は、公開中の全バージョンの合計として考える必要があります。
