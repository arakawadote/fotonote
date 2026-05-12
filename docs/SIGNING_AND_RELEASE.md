# 署名キーとリリースAAB作成

FotoNoteをGoogle Playの内部テストへ出すには、署名付きAABが必要です。

## 重要

- 署名キーは絶対にGitHubへpushしないでください。
- 署名キーとパスワードを失うと、同じアプリとして更新できなくなる可能性があります。
- このプロジェクトでは `keystore.properties`、`*.jks`、`keystores/` を `.gitignore` で除外しています。

## 1. 署名キーを作成する

Android Studioで以下を開きます。

```txt
Build > Generate Signed App Bundle / APK
```

選択内容:

- `Android App Bundle` を選択
- `Create new...` で新規キーを作成
- Key store path: `C:\Users\under\Desktop\project-root\keystores\fotonote-release.jks`
- Key alias: `fotonote`
- Validity: 25年以上
- 本名を避けたい場合、証明書の名前欄は `arakawadote` や `FotoNote` などにする

パスワードは安全な場所に保管してください。

## 2. keystore.propertiesを作成する

`keystore.properties.example` をコピーして、プロジェクト直下に `keystore.properties` を作成します。

```txt
storeFile=keystores/fotonote-release.jks
storePassword=作成したキーストアのパスワード
keyAlias=fotonote
keyPassword=作成したキーのパスワード
```

`keystore.properties` はGitHubへpushしません。

## 3. リリースAABを作成する

PowerShellで以下を実行します。

```powershell
cd C:\Users\under\Desktop\project-root
.\gradlew.bat :app:bundleRelease
```

成功すると、AABは以下に作成されます。

```txt
app\build\outputs\bundle\release\app-release.aab
```

このAABをGoogle Play Consoleの内部テストへアップロードします。

`keystore.properties` が未作成の場合、Releaseビルドは安全のため停止します。
