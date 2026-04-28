# news-app 仕様書 v0.1

## 1. アプリ概要

- アプリ名: news-app
- 画面表示名: News Focus
- コンセプト: AI・IT・株式投資・経済ニュースを、テーマ別に見やすくチェックできるニュースアプリ。
- 目的: 最新ニュースをテーマボタンやキーワード検索で簡単に探し、気になる記事を保存してあとで見返せるようにする。

## 2. 想定ユーザー

主なユーザーは自分自身。

用途:
- AI関連ニュースをチェックする
- IT、Java、半導体ニュースを確認する
- 株式投資に関係するニュースを見る
- 日本株、米国株、決算、為替、金利ニュースを見る
- 気になる記事を保存する

## 3. MVP機能

必須機能:
- トップページ表示
- テーマボタン表示
- ニュース一覧表示
- キーワード検索
- テーマ検索
- 元記事リンク
- 記事保存
- 保存記事一覧
- 保存記事削除
- Render公開

## 4. 今回は作らない機能

- ログイン
- 管理画面
- AI要約
- 記事全文取得
- 株価チャート
- ポートフォリオ管理
- 複数ユーザー対応
- メモ機能

理由:
まずは15時間以内に完成することを優先し、ニュース閲覧・検索・保存に集中するため。

## 5. 画面仕様

### 5.1 トップページ

URL:
`GET /`

目的:
ニュースをテーマ別・キーワード別に見やすく表示する。

表示内容:
- アプリ名
- 説明文
- キーワード検索フォーム
- AI・ITテーマボタン
- 投資・経済テーマボタン
- ニュースカード一覧

AI・ITテーマ:
- AI
- 生成AI
- IT
- Java
- 半導体

投資・経済テーマ:
- 株式投資
- 日本株
- 米国株
- 決算
- 為替
- 金利

ニュースカード表示項目:
- テーマラベル
- タイトル
- 概要
- 媒体名
- 公開日
- 続きを読むボタン
- 保存ボタン

### 5.2 保存記事一覧ページ

URL:
`GET /saved`

目的:
保存したニュースをあとで見返す。

表示内容:
- 保存記事一覧
- タイトル
- 概要
- 媒体名
- 公開日
- 保存日
- 元記事リンク
- 削除ボタン

## 6. URL設計

ニュース表示:
- `GET /` : トップページ表示
- `GET /?keyword=AI` : キーワード検索
- `GET /?theme=ai` : テーマ検索

保存記事:
- `POST /articles/save` : 記事保存
- `GET /saved` : 保存記事一覧
- `POST /saved/{id}/delete` : 保存記事削除

## 7. DB仕様

テーブル名:
`saved_articles`

カラム:
- `id`: BIGINT, 主キー
- `title`: VARCHAR(500), 記事タイトル
- `description`: VARCHAR(2000), 記事概要
- `url`: VARCHAR(1000), 元記事URL
- `image_url`: VARCHAR(1000), 画像URL
- `source_name`: VARCHAR(255), 媒体名
- `published_at`: VARCHAR(100), 公開日
- `theme`: VARCHAR(50), 保存時のテーマ
- `saved_at`: TIMESTAMP, 保存日時

制約:
- `url` は UNIQUE にする
- 同じ記事を重複保存しない

## 8. 環境仕様

ローカル開発:
- 実行環境: VSCode
- Java: 17
- DB: H2
- Profile: local

本番環境:
- 実行環境: Render
- DB: Neon PostgreSQL
- Profile: prod

## 9. 設定ファイル方針

使用する設定ファイル:
- `application.properties`
- `application-local.properties`
- `application-prod.properties`

`application.properties`:
- 共通設定
- `spring.application.name=news-app`
- `spring.profiles.active=${SPRING_PROFILES_ACTIVE:local}`
- `news.api.key=${NEWS_API_KEY:}`
- `news.api.base-url=https://api.currentsapi.services/v1`

`application-local.properties`:
- ローカルH2用
- `spring.datasource.url=jdbc:h2:mem:newsdb`
- `spring.datasource.driver-class-name=org.h2.Driver`
- `spring.datasource.username=sa`
- `spring.datasource.password=`
- `spring.h2.console.enabled=true`
- `spring.h2.console.path=/h2-console`

`application-prod.properties`:
- Render + Neon PostgreSQL用
- `spring.datasource.url=${DATABASE_URL}`
- `spring.datasource.username=${DATABASE_USERNAME}`
- `spring.datasource.password=${DATABASE_PASSWORD}`
- `spring.datasource.driver-class-name=org.postgresql.Driver`
- `spring.h2.console.enabled=false`

## 10. パッケージ構成

`src/main/java/com/example/newsapp`
- `NewsAppApplication.java`
- `controller/HomeController.java`
- `controller/SavedArticleController.java`
- `service/NewsService.java`
- `service/SavedArticleService.java`
- `client/NewsApiClient.java`
- `mapper/SavedArticleMapper.java`
- `model/NewsArticle.java`
- `model/SavedArticle.java`
- `form/ArticleSaveForm.java`

## 11. ブランチ運用

ブランチ:
- `main`: 本番用
- `develop`: 開発統合用
- `feature/*`: 機能開発用

開発ルール:
- `main` へ直接コミットしない
- `develop` から `feature` ブランチを作る
- `feature` で作業する
- 動作確認後に `develop` へマージする
- 本番公開できる状態になったら `main` へマージする

## 12. 開発順序

1. トップページの初期画面を作る
2. 環境別設定を作る
3. ニュースAPI接続を作る
4. AIニュースを表示する
5. テーマボタン検索を作る
6. キーワード検索を作る
7. 記事保存を作る
8. 保存記事一覧と削除を作る
9. Render公開する

## 13. 最初の実装タスク

タスク名:
トップページの初期画面作成

ブランチ:
`feature/top-page`

目的:
ニュースAPI接続前に、アプリの見た目と画面構成を作る。

触るファイル:
- `src/main/java/com/example/newsapp/controller/HomeController.java`
- `src/main/resources/templates/index.html`

完了条件:
- `http://localhost:8080/` でトップページが表示される
- Whitelabel Error Page が出ない
- News Focus のタイトルが表示される
- テーマボタンが表示される
- ダミーニュースカードが3件表示される
- 起動確認後にコミットする

コミットメッセージ:
トップページの初期画面を作成
