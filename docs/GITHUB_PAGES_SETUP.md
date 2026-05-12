# GitHub Pages設定メモ

FotoNoteの公開用Webサイトは `site/` にあります。

## 公開手順

1. GitHubにリポジトリを作成します。
2. このプロジェクトをGitHubへpushします。
3. GitHubのリポジトリで `Settings` > `Pages` を開きます。
4. `Build and deployment` の `Source` を `GitHub Actions` にします。
5. `Actions` タブで `Deploy GitHub Pages` が成功することを確認します。

公開URLは通常、次の形式になります。

```txt
https://<GitHubユーザー名>.github.io/<リポジトリ名>/
```

プライバシーポリシーURLは次の形式です。

```txt
https://<GitHubユーザー名>.github.io/<リポジトリ名>/privacy.html
```

## 公開前に差し替えるもの

- `site/privacy.html` の問い合わせ先
- 必要であれば `site/index.html` の説明文
- Play Consoleに登録するプライバシーポリシーURL
