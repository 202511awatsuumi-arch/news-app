# News Focus

## 概要
AI・半導体・米国テック企業ニュースを見やすくチェックできるニュースWebアプリです。  
Currents APIからニュースを取得し、気になる記事を保存してあとで見返せます。

## 公開URL
https://news-app-nqh1.onrender.com/

## GitHub
https://github.com/202511awatsuumi-arch/news-app

## 作成目的
- Java / Spring Boot を使ったWebアプリ開発の学習
- 外部API連携の実装
- Renderを使った本番公開
- Neon PostgreSQLを使った本番DB運用
- Dockerデプロイの経験
- ポートフォリオとして公開できるアプリ作成

## 主な機能
- 最新ニュース表示
- テーマ別ニュース表示
  - AI
  - 生成AI
  - NVIDIA
  - TSMC
- キーワード検索
- 元記事URLへのリンク
- 記事保存
- 保存記事一覧
- 保存記事削除
- 重複保存防止
- API取得失敗時のフォールバック表示

## 使用技術
### バックエンド
- Java 17
- Spring Boot
- Spring MVC
- Thymeleaf
- MyBatis

### データベース
- H2 Database（ローカル開発）
- Neon PostgreSQL（本番環境）

### 外部API
- Currents API

### インフラ・デプロイ
- Render
- Docker
- GitHub

### 開発環境
- VSCode
- SourceTree
- Codex

## アプリ構成
- controller
  - HomeController
  - SavedArticleController
- service
  - NewsService
  - SavedArticleService
- client
  - NewsApiClient
- mapper
  - SavedArticleMapper
- model
  - NewsArticle
  - SavedArticle
- form
  - ArticleSaveForm
- templates
  - index.html
  - saved.html

## DB設計
`saved_articles` テーブルに、保存記事のメタ情報を保持します。  
記事本文そのものではなく、一覧表示や再アクセスに必要な情報を保存する構成です。

### カラム
- id
- title
- description
- url
- image_url
- source_name
- published_at
- theme
- saved_at

### 制約
- `url` は `UNIQUE`
- 同じ記事を重複保存しない

## 環境変数
実値は設定せず、環境変数名のみ管理します。

### ローカル
- NEWS_API_KEY

### 本番Render
- SPRING_PROFILES_ACTIVE=prod
- NEWS_API_KEY
- DATABASE_URL
- DATABASE_USERNAME
- DATABASE_PASSWORD

APIキーやDB接続情報はGitHubにコミットしません。

## ローカル起動方法
PowerShell前提です。

1. Java 17 を確認
2. プロジェクトを開く
3. NEWS_API_KEY を一時設定
4. 起動
5. localhostにアクセス

```powershell
cd C:\academia\src\news-app

$env:NEWS_API_KEY="Currents APIキー"

.\mvnw spring-boot:run
```

アクセスURL:
http://localhost:8080/

## 本番デプロイ
Render + Docker + Neon PostgreSQL 構成で公開しています。

### Render設定
- Language: Docker
- Branch: main
- Region: Singapore
- Instance Type: Free

### Neon
- PostgreSQL
- Free plan
- Singapore region
- `saved_articles` テーブルを作成して使用

## 工夫した点
機密情報を安全に扱うため、APIキーやDB接続情報はすべて環境変数で管理しました。  
また、ローカルはH2、本番はNeon PostgreSQLと用途を分けることで、開発効率と本番運用の再現性を両立しています。  
ニュース取得では、Currents APIで実際に取得しやすいテーマに画面を整理し、取得しづらいキーワードには補助キーワードを順番に試す実装にしました。  
保存機能ではURL重複を防止し、Dockerfileを用意してRenderで安定してデプロイできる構成にしています。

## 苦労した点
日本語キーワードではニュースAPIの検索結果が0件になるケースがあり、期待した結果が安定しない時期がありました。  
ログで実際の取得件数を確認しながら、取得できるキーワードに合わせてテーマを整理して改善しています。  
また、本番DBで `saved_articles` テーブル未作成により `/saved` が500になる問題がありましたが、Neon SQL Editorでテーブルを作成して解決しました。

## 今後の改善案
- UIデザイン改善
- 保存記事へのメモ機能
- 重要度・タグ付け
- ニュース取得結果のキャッシュ
- テーマ追加
- API候補の比較・切り替え

## 注意事項
- 無料枠で運用しているため、API回数やRender/Neonの制限に注意
- APIキーやDBパスワードはREADMEに書かない
- 記事本文全文は保存せず、タイトル・概要・URLなど必要情報のみ保存する
