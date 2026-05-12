# リリース前チェックリスト：FotoNote

最終更新日：2026-05-11

このチェックリストは、現在のMVP実装をGoogle Play公開に近づけるための確認項目です。公開直前にはGoogle Play Console上の表示と最新ポリシーを再確認してください。

## 現在の実装状況

- [x] アプリ名：FotoNote
- [x] 仮パッケージ名：`com.example.exifframe`
- [x] `minSdk`：26
- [x] `targetSdk`：35
- [x] 画像選択：OpenDocument
- [x] EXIF読み取り：AndroidX ExifInterface
- [x] GPS情報：読み取り対象には含むがUIには表示しない
- [x] 画像処理：端末内で完結
- [x] 書き出し：JPEG / MediaStore保存
- [x] 書き出し画像にUIボタンを含めない
- [x] 書き出し画像に広告を含めない
- [x] アプリアイコン設定
- [ ] 正式パッケージ名の決定
- [ ] 署名キーの作成と安全な保管
- [ ] リリースビルド作成
- [ ] Google Play Console登録
- [ ] まずは内部テストで配布する
- [x] アプリ内プライバシーポリシーページを表示できる

## Google Play要件

- [x] 対象APIレベル
  - 現在 `targetSdk = 35`。
  - 2025-08-31以降、新規アプリとアップデートはAndroid 15 / API 35以降が必要。

- [ ] アプリの正式パッケージ名
  - 現在は `com.example.exifframe`。
  - 公開前に正式なIDへ変更する。
  - 一度公開すると基本的に同じアプリとして変更できないため、公開前に確定する。

- [ ] Play ConsoleのData safety
  - Play Consoleで必ず回答する。
  - 現在のMVPは画像・EXIFを端末内で処理し、サーバー送信しない。
  - AdMob追加後は、広告SDKによるデータ収集・共有の扱いを反映する。

- [ ] プライバシーポリシー
  - Google Playでは、個人情報を扱わないアプリでもプライバシーポリシーURLが必要。
  - 公開URLはPDFではなく、誰でもアクセスでき、編集不可のページにする。
  - アプリ内表示はユーザー向けには有効だが、Play Console用の公開URLは別途必要。
  - 草案は `docs/PRIVACY_POLICY_DRAFT.md` に作成済み。

## 内部テスト方針

- [x] すぐ本公開せず、まずはGoogle Play Consoleの内部テストで進める。
- [ ] 内部テスター用Googleアカウントを用意する。
- [ ] 内部テスト用ビルドを作成する。
- [ ] アプリ内のプライバシーポリシー表示を確認する。
- [ ] Play Consoleで表示される開発者情報を確認する。
- [ ] 本公開前にデベロッパー名、問い合わせ先、プライバシーポリシーURLを確定する。

## 権限・プライバシー

- [x] 広範囲な画像読み取り権限を要求しない。
- [x] 画像選択はユーザーが選んだファイルだけを扱う。
- [x] 画像とEXIFを外部サーバーへ送信しない。
- [x] GPS情報を画面にも書き出し画像にも表示しない。
- [x] ログイン、アカウント作成、クラウド保存なし。
- [x] 課金、サブスクリプション、買い切りなし。
- [x] 内部テスト版のAdMobテスト広告について、アプリ内プライバシーポリシーへ記載する。
- [ ] 本番AdMob導入前に、プライバシーポリシーとData safetyを更新する。

## AdMob導入前チェック

- [ ] AdMobアカウントを用意する。
- [ ] AdMobアプリIDを取得する。
- [ ] バナー広告ユニットIDを取得する。
- [x] 開発中はGoogleのテスト広告IDを使う。
- [ ] 本番リリース前に本番IDへ差し替える。
- [x] 画面下固定バナーとして表示する。
- [ ] 広告読み込み失敗時もアプリがクラッシュしない。
- [ ] 書き出し画像に広告が含まれないことを再確認する。

## ストア掲載素材

- [ ] アプリ名
- [ ] 短い説明
- [ ] 詳細説明
- [ ] アプリアイコン
- [ ] フィーチャーグラフィック
- [ ] スマホスクリーンショット
- [ ] プライバシーポリシーURL
- [ ] サポート連絡先

## 動作確認

- [x] 画像を選択できる。
- [x] 横写真を表示できる。
- [x] 縦写真を正しい向きで表示できる。
- [x] EXIFあり画像でメタデータを表示できる。
- [x] EXIFなし画像でもクラッシュしない。
- [x] 画像を書き出せる。
- [x] 書き出し画像をギャラリー保存できる。
- [ ] 複数端末サイズで確認する。
- [ ] 実機で確認する。
- [ ] リリースビルドで確認する。

## 公式参照

- Google Play対象APIレベル要件：https://developer.android.com/google/play/requirements/target-sdk?hl=ja
- Google Play User Data / Privacy Policy：https://support.google.com/googleplay/android-developer/answer/10144311
- Google Play Data safety：https://support.google.com/googleplay/android-developer/answer/10787469
- AdMob App ID / Ad Unit ID：https://support.google.com/admob/answer/7356431
