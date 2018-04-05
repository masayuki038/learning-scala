# 序章

- 7章で、Scalaはビルトインの制御抽象概念を多く持っていないことを指摘した、何故なら自分でそれを作れるからである
- この章では、新しい制御抽象概念を作成する為に関数値を適用する方法を示す

# 9.1 Reducing code duplication

- すべての関数は、関数の呼び出し毎に変わらない共通部分と、呼び出し毎に変わる非共通部分がある
- 共通部分は関数の本体であるのにた対し、非共通部分は引数を通じて提供されなければならない
- 引数に関数値を使う場合、アルゴリズムの非共通部分は、それ自身が他のアルゴリズムである
- そのような関数の呼び出し毎に、引数として異なった関数値を渡し、呼び出された関数はその選択のときに、渡された関数値を呼び出す
- これらの高階関数-関数をパラメータとして取る-は、コードを凝縮して簡潔にする特別な機会を与える
- 公開関数の一つの大きな恩恵は、コードの重複を減らす制御抽象概念を作成することができる
- 例えば、ファイルブラウザを書く時、ファイルを検索するAPIを提供したいと思う
- まず、特定の文字列の最後の名称を持つファイルの検索ができるような機能を追加する
- これは".scala"というファイルの拡張子をもつすべてのファイルを検索できるようにする

```scala
  object FileMatcher {
    private def filesHere = (new java.io.File(".")).listFiles

    def filesEnding(query: String) =
      for (file <- filesHere; if file.getName.endsWith(query))
        yield file
  }
```

- `filesEnding`メソッドは`fileHere`というprivateなヘルパーメソッドを使ってカレントディレクトリのすべてのファイルのリストを取得する
- そして、それぞれのファイル名がユーザが指定した`query`で終わるかどうかでフィルタする
- `filesHere`はprivateで、`filesEnding`は`FileMatcher`で定義された唯一アクセスできるメソッドであり、ユーザに提供するAPIである
- ここまでは順調で、まだコードの重複はない
- けれども後になって、ファイル名のどんな部分での検索できるように決めた
- これはユーザがどのようにファイル名をつけたか覚えられない時に効果がある

```scala
  def filesContaining(query: String) =
    for (file <- filesHere; if file.getName.contains(query))
      yield file
```

- この関数は`fileEnding`にかなり似ている
- `filesHere`でファイルを探し、名前をチェックし、名前がマッチしたらそのファイルを返す
- この関数は`endsWith`ではなく`contains`を使っているところだけが違う
- 数ヶ月過ぎて、プログラムはよりよくなった
- 正規表現で検索したいという少数のパワーユーザのリクエストに降参した

```scala
  def filesRegex(query: String) =
    for (file <- filesHere; if file.getName.matches(query))
      yield file
```

- 経験の多いプログラマたちは、この繰り返しに気が付き、それを一般的なヘルパー関数に分解することができないかと考えた
- しかしながら、それを明らかにすることはできなかった

```scala
  def filesMatching(query: String, method) =
    for (file <- filesHere; if file.getName.method(query))
      yield file
```

- このアプローチは動的言語で機能するが、Scalaはこれを認めない。どうするか？

# 単語
- vary: 変わる、変化する
- vary from ～: ～とは異なる
- condense: 凝縮させる、要約する、簡約する
- so far so good: 今のところは順調
- sloppy: ずさんな、乱雑な、薄くて味気ない
- immense: 巨大な、計り知れない、すてきな
- gìve ín: 提出する、手渡す、降参する
- factor(動詞): 因数に分解する
