# FotoNote

Android向けのKotlin / Jetpack Composeアプリです。

写真を選ぶだけで、EXIF情報を読み取り、カメラ情報付きの白フレーム画像を書き出せます。

## 現在の設定

- アプリ名: FotoNote
- パッケージ名: `com.arakawadote.fotonote`
- 最小対応Android: Android 8.0 / API 26
- 画面向き: 縦画面メイン
- テンプレート: Classic White / Soft Gray / Noir
- 書き出しサイズ: 幅1080px / 高さ可変

## 実装済み

- Androidプロジェクトの基本構成
- Jetpack Composeのメイン編集画面
- 画像選択
- EXIF読み取り
- 画像の向き補正
- Classic Whiteテンプレートのプレビュー
- 3種類のテンプレート選択
- 選択テンプレートを反映した画像書き出し
- 画像比率に合わせたフレーム生成
- MediaStoreでのギャラリー保存
- アプリアイコン差し替え
- プライバシーポリシー画面
- AdMobテストバナー広告

## GitHub Pages

公開用Webサイトは `site/` に配置しています。

- トップページ: `site/index.html`
- プライバシーポリシー: `site/privacy.html`
- GitHub Actions設定: `.github/workflows/pages.yml`

GitHub Pagesで公開すると、プライバシーポリシーURLは通常以下の形式になります。

```txt
https://<GitHubユーザー名>.github.io/<リポジトリ名>/privacy.html
```

問い合わせ先は `developmentbyarakawadote@gmail.com` を設定しています。

## リリース前メモ

- Play Consoleはまず内部テストで進める
- 本番用AdMob IDに差し替える
- パッケージ名: `com.arakawadote.fotonote`
- 署名設定を作成する
- GitHub Pagesの公開URLをPlay Consoleに登録する

詳細は [docs/RELEASE_CHECKLIST.md](docs/RELEASE_CHECKLIST.md) を参照してください。
署名付きAABの作成手順は [docs/SIGNING_AND_RELEASE.md](docs/SIGNING_AND_RELEASE.md) を参照してください。
ストア掲載文案は [docs/PLAY_STORE_LISTING_DRAFT.md](docs/PLAY_STORE_LISTING_DRAFT.md)、Data safety回答案は [docs/DATA_SAFETY_DRAFT.md](docs/DATA_SAFETY_DRAFT.md) を参照してください。
