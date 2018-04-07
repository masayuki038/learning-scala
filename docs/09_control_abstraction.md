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
- メソッドの名前を値として渡すことはできないが、呼びたいメソドを関数値として渡すことで同じ効果が得られる
- このケースでは、単にクエリに対してファイル名をチェックするだけのメソッドに、matcherパラメータを追加することができる
- このメソッドのバージョンでは、`if`節がファイル名をチェックする為にmatcherを使うようになっている
- 厳密には、このチェックはmatcherの仕様に依存する
- matcher関数は、`name`と`query`の2つの文字列を取り、booleanを返す
- だから関数の型は`(String, String) => Boolean`である
- 新しい`filesMatching`ヘルパーメソッドを呼び出すことで、3つの検索メソッドをシンプルにすることができる

```scala
  def filesEnding(query: String) =
    filesMatching(query, _.endsWith(_))

  def filesContaining(query: String) =
    filesMatching(query, _.contains(_))

  def filesRegex(query: String) =
    filesMatching(query, _.matches(_))
```

- この例の関数リテラルは前章で紹介したプレースホルダー記法を使っているが、まだ自然に感じられないかもしれない
- それゆえ、ここではこの例でプレースホルダがどのように使われているかを明確にする
- `filesEnding`メソッドで使われている``_.endsWith(_)`という関数リテラルは、以下と同様である。

```scala
(fileName: String, query: String) => fileName.endsWith(query)
```

- `fileMatching`は2つの文字列と必要とするが、型の指定をする必要はない
- それゆえ、`(fileName, query) => fileName.endsWith(query)`と書くことができる
- それぞれのパラメータは関数本体で一度しか使われない上、最初のパラメータである`fileName`は関数内で最初に、二番目のパラメータである`query`は関数内で2番目に使われているので、プレースホルダ(_)を使える
- `_.endsWith(_)`は、最初のアンダースコアは最初のパラメータであるファイル名、二番目のアンダースコアは2番目のパラメータであるquery文字列になる
- このコードはすでにシンプル化されているが、実際にはもっと短くできる
- `fileMatching`に渡されている`query`は、matcher関数に渡される以外に何もしていない
- 呼び出し側はすでにクエリを知っているので、この行ったり来たりは不要である
- 単に`fileMatching`とmatcherから`query`を取り除くと、以下のようになる

```scala
    object FileMatcher {
      private def filesHere = (new java.io.File(".")).listFiles

      private def filesMatching(matcher: String => Boolean) =
        for (file <- filesHere; if matcher(file.getName))
          yield file

      def filesEnding(query: String) =
        filesMatching(_.endsWith(query))

      def filesContaining(query: String) =
        filesMatching(_.contains(query))

      def filesRegex(query: String) =
        filesMatching(_.matches(query))
    }
```

- この例は、従来コードの重複を削減することが難しかったケースでも、ファーストクラス関数を用いて解消する方法を説明している
- 例えばJavaでは、一つの文字列を受け取りBooleanを返すメソッドを含むinterfaceを作成して、無名クラスを作成し、`fileMatching`関数に渡すことができた
- このアプローチはコードの削減はできるだろうが、同時に新しいコードを沢山追加することになる
- それゆえ、その恩恵はコストの価値はなく、なおコードの重複は残り続けるだろう
- また、この例はクロージャがどのようにコードの重複を削減するかを説明している
- `_.endsWith(_)`や`_.contains(_)`のような前の例で使われている関数リテラルは、実行時にクロージャではない関数値を生成している
- なぜなら、それらはy自由関数をキャプチャしていない
- 例えば、`_endsWith(_)`の式で使われている両変数は、関数に渡された引数を意味するアンダースコアで表されている
- それゆえ、`_endsWith(_)`は2つの束縛された変数を使っているが、自由変数は使っていない
- 反対に、`_.endsWith(query)`という関数リテラルは、アンダースコアで表された1つの束縛された変数と、`query`という名前の一つの自由変数が含まれている
- それは、コードをもっとシンプルにする為に、`fileMatching`関数から`query`パラメータを除去するクロージャをScalaがサポートするので、そのようにしただけであ

# 単語
- vary: 変わる、変化する
- vary from ～: ～とは異なる
- condense: 凝縮させる、要約する、簡約する
- so far so good: 今のところは順調
- sloppy: ずさんな、乱雑な、薄くて味気ない
- immense: 巨大な、計り知れない、すてきな
- gìve ín: 提出する、手渡す、降参する
- factor(動詞): 因数に分解する
- sole: 唯一の、だけの、未婚の
- clarification: 明確化、説明、浄化
- precisely: 正確に、厳密に、詳細に
- forth: 前へ、先へ、前方へ
- back and forth: 堂々巡りの議論、後退したり前進したりの、行ったり来たりの
- as well: なお、おまけに、その上
- thereby: それによって