IgapyonV3 Maven Plugin のアーキテクチャ
=====================================

概要
----
このリポジトリは、Igapyon Diary System v3 のコアライブラリを Maven プラグインとして
利用できるようにするラッパーです。Maven ゴールとして、日記ディレクトリの初期化、
テンプレートの初期化、Markdown から HTML 生成を提供します。

スコープと境界
--------------
- 本プラグインは Maven ゴールの実行とパラメータの受け渡しに特化しています。
- 日記の処理やファイル生成の実体は igapyonv3 コアライブラリにあります。
- パースやレンダリングのロジックはこのプラグイン内にはありません。

主要コンポーネント
------------------
- `src/main/java/jp/igapyon/diary/igapyonv3/plugin/IgGenerateMojo.java`
  - Maven ゴール: `generate`
  - `IgapyonV3Settings` を構築し `IgDiaryProcessor` を実行します。
- `src/main/java/jp/igapyon/diary/igapyonv3/plugin/IgInitMojo.java`
  - Maven ゴール: `init`
  - `IgInitDiaryDir` により日記ディレクトリ構成を作成します。
- `src/main/java/jp/igapyon/diary/igapyonv3/plugin/IgInittemplateMojo.java`
  - Maven ゴール: `inittemplate`
  - `IgInittemplateDiaryDir` によりテンプレートを作成します。

実行フロー
----------
Generate (HTML 生成):
1. `IgGenerateMojo.execute()` が `basedir` を解決し `rootdir` を正規化します。
2. `outputhtmldir` が未指定の場合、`${project.build.directory}/html` が既定値です。
3. `IgapyonV3Settings` に `rootdir` と `outputhtmldir` を設定します。
4. `IgDiaryProcessor.process()` が変換処理を実行します。

Init (ディレクトリ初期化):
1. `IgInitMojo.execute()` が `basedir` を解決し `rootdir` を正規化します。
2. `IgInitDiaryDir.process()` が初期ディレクトリ構成を作成します。

Inittemplate (テンプレート初期化):
1. `IgInittemplateMojo.execute()` が `basedir` を解決し `rootdir` を正規化します。
2. `IgInittemplateDiaryDir.process()` がテンプレートを作成します。

設定パラメータ
--------------
- `generate.basedir` (File, 既定: `${project.basedir}`)
- `generate.outputhtmldir` (File, 既定: `${project.build.directory}/html`)
- `init.basedir` (File, 既定: `${project.basedir}`)
- `inittemplate.basedir` (File, 既定: `${project.basedir}`)

ビルドとパッケージング
----------------------
- `pom.xml` にプラグインの座標と `maven-plugin` パッケージングを定義します。
- `maven-plugin-annotations` を使用して Mojo メタデータを定義します。
- Java 1.6 を対象にコンパイルします。

外部依存
--------
- `jp.igapyon.diary:igapyonv3` (コア実装)
- Maven プラグイン API およびアノテーション

igapyonv3 本体の内部構造 (参考)
------------------------------
本プラグインが呼び出す igapyonv3 本体側の処理概要を、簡潔にまとめます。

概要
----
- igapyonv3 は Java 製の静的サイト/ブログ生成ツールです。
- 主な処理は `.src.md` を起点とした変換パイプラインと、インデックス/キーワード/RSS の生成です。

主な処理フロー (IgDiaryProcessor)
---------------------------------
1. `settings.src.md` の読み込みと展開
2. 今日の日記の自動生成 (設定で有効化時)
3. `keyword` / `memo` ディレクトリの作成
4. インデックス用 Atom の生成
5. キーワード用 Atom の生成
6. 必要に応じてキーワード `.md` の生成
7. `.src.md` → `.md` / `.html.md` 変換
8. Markdown → HTML 変換 (設定で有効化時)

主要クラス
----------
- `jp.igapyon.diary.igapyonv3.IgDiaryProcessor`
  - 全体処理の起点。設定読み込みと変換処理の流れを統括します。
- `jp.igapyon.diary.igapyonv3.mdconv.DiarySrcMd2MdConverter`
  - FreeMarker 展開後テキストから `.md` を生成します。
- `jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2Html`
  - Markdown を解析して HTML を出力します。
- `jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings`
  - ルート/出力ディレクトリ、各種フラグなどの設定を保持します。

設定の要点 (settings.src.md)
----------------------------
- `setGeneratetodaydiary("true")` で今日の日記を自動生成します。
- `setGeneratekeywordifneeded("true")` で不足キーワード `.md` を生成します。
- `setConvertmarkdown2html("true")` で HTML 生成を有効化します。
- `setDuplicatefakehtmlmd("true")` は `.html.md` 複製生成ですが deprecated です。

出力ディレクトリ規約
--------------------
- `.src.md` はソース原稿、`.md` は生成 Markdown、`.html` は生成 HTML です。
- `target/html` は既定の HTML 出力先です。
- `target/md2html` は運用 pom の変換出力先として利用されます。

運用 pom 実行フロー (参考)
--------------------------
1. `igapyonv3-maven-plugin:generate` が `generate-resources` で実行されます。
2. `maven-antrun-plugin:run` で `KeywordMdTextGenerator` / `IgapyonMd2Html` を起動します。
3. 生成物を公開先へコピーします。
