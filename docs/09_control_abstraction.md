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

# 9.2 Simplifying client code

- 前回の例は、APIを作る時に、高階関数がコードの重複を減らすことができることを説明した
- 高階関数を使うもう一つの重要な事項は、API自身に高階関数を含めることにより、クライアントコードを短くすることができる
- 良い例は、Scalaのコレクション型のメソッドで提供されている
- 指定された値がコレクションに含まれるかどうかを返す`exists`について考える
- 要素の検索は、まず変数をfalseで初期化し、それぞれの要素をチェックして、見つけたら変数をtrueにする
- これは、負の数が含まれているかどうかを返すのにこのアプローチを使っているメソッドである

```scala
  def containsNeg(nums: List[Int]): Boolean = {
    var exists = false
    for (num <- nums)
      if (num < 0)
        exists = true
    exists
  }
```

- けれども、そのメソッドの定義をもっと短く書く方法は、このように渡されたリストの高階関数である`exists`メソッドを呼び出すことである

```scala
  def containsNeg(nums: List[Int]) = nums.exists(_ < 0)
```

- `exists`メソッドは制御抽象概念を表している
- それは組み込みの`for`や`while`よりもむしろScalaのライブラリによって提供されているループ構造である
- 前の章で、高階関数である`fileMatching`はコードの重複を削減した
- `exists`メソッドは同じような恩恵をもたらすが、`exists`はScalaのコレクションAPIで公開されているので、削減されるコードの重複はAPIのクライアントコード側である
- もし`exists`が無ければ、奇数の値が含まれているかどうかをテストする`containsOdd`メソッドをを書く必要がある

```scala
  def containsOdd(nums: List[Int]): Boolean = {
    var exists = false
    for (num <- nums)
      if (num % 2 == 1)
        exists = true
    exists
  }
```

- `containsNeg`と比較すると、テスト条件だけが違うことが分かるだろう
- 代わりに`exists`を使うと、以下のようになる

```scala
  def containsOdd(nums: List[Int]) = nums.exists(_ % 2 == 1)
```

- このバージョンのコードの内容は、検索する条件が違うことを除いて`containsNeg`の`exists`バージョンと同じである
- さらに、ループ構造は`exists`メソッド自身によって取り除かれるので、コードの重複はより少ない
- Scalaの標準ライブラリには多くの他のループメソッドがある
- `exists`と同様に、使いどころが分かればコードを短くすることができる

# 9.3 Currying

- 1章で、Scalaはネイティブにサポートされているような新しい制御抽象概念を作成することができると言った
- ここまで見てきた例では、実際に制御抽象概念ではあるけれども、それがネイティブにサポートされていると思う人は誰もいないだろう
- より言語拡張のように感じられる制御抽象概念を作成する方法を理解する為に、まずはカリー化と呼ばれる関数プログラミングテクニックを理解する必要がある
- カリー化された関数は一つではなく複数の引数を取る
- 以下の関数は、`x`と`y`の2つの引数を取る、カリー化されていない例である

```scala
    scala> def plainOldSum(x: Int, y: Int) = x + y
    plainOldSum: (Int,Int)Int

    scala> plainOldSum(1, 2)
    res4: Int = 3
```

- 反対に、以下の例は似たような関数でカリー化されている
- 2つのIntパラメータのある一つのリストの代わりに、この関数にそれぞれ1つのパラメータを取る2つのリストを適用する

```scala
    scala> def curriedSum(x: Int)(y: Int) = x + y
    curriedSum: (Int)(Int)Int

    scala> curriedSum(1)(2)
    res5: Int = 3
```

- `curriedSum`を呼び出した時に何が起こったかというと、実際には2つの関数を次々に呼び出している
- 最初の関数は`x`という名前の一つのIntパラメータを取り、2つ目の関数の為に関数値を返す
- 二つ目の関数は`y`という名前のIntパラメータを取る
- これは、`first`という名前の関数は、`curriedSum`の最初の関数呼び出しを表したものである

```scala
  scala> def first(x: Int) = (y: Int) => x + y
  first: (Int)(Int) => Int
```

- `first`に1を適用すると、二番目の関数が生成される

```scala
  scala> val second = first(1)
  second: (Int) => Int = <function>
```

- 二番目の関数に2を適用すると、結果が生成される

```scala
  scala> second(2)
  res6: Int = 3
```

- `first`と`second`の関数はまさにカリー化のプロセスを説明している
- それらは直接`curriedSum`関数とつながっていない
- それにも関わらず、`curriedSum`の2番目の関数を実際に取得する方法がある
- 部分適用した関数`curriedSum`を使う為に、プレースホルダ記法を使う

```scala
  scala> val onePlus = curriedSum(1)_
  onePlus: (Int) => Int = <function>
```

- `curriedSum(1)_`のアンダースコアは2番目のパラメータリストのプレースホルダである
- その結果は、呼び出すと単に1に引数のIntの値を加算して結果を返す関数への参照である

```scala
  scala> onePlus(2)
  res7: Int = 3
```

- これは2に引数のInt値を加算する関数を取得する方法である

```
  scala> val twoPlus = curriedSum(2)_
  twoPlus: (Int) => Int = <function>

  scala> twoPlus(2)
  res8: Int = 4
```

# 9.4 Writing new control structures

- 関数がファーストクラスである言語において、言語の文法が決まっていても、新しい制御構造を作ることができる
- それに必要なことは、関数を引数にとるメソッドを作ることである
- 例えば、ここに操作を2回繰り返して結果を返す、`twice`という制御構造がある

```scala
  scala> def twice(op: Double => Double, x: Double) = op(op(x))
  twice: ((Double) => Double,Double)Double

  scala> twice(_ + 1, 5)
  res9: Double = 7.0
```

- この例の`op`の型は`Double => Double`で、それは一つのDoubleな引数を取り、Doubleの結果を返す
- コードのあちらこちらで繰り返される制御パターンを見つける度に、新しい制御構造を実装することを検討するべきである
- 前章ではとても特別な制御パターンを持つ`filesMatching`があった
- より広く使われているコードパターンである、リソースをオープンし、操作し、リソースをクローズするケースについて考えてみる
- 次のようなメソッドを使う制御抽象概念を捉えることができる

```scala
  def withPrintWriter(file: File, op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
```

- これは以下のようにして使う

```scala
  withPrintWriter(
    new File("date.txt"),
    writer => writer.println(new java.util.Date)
  )
```

- このメソッドを使う利点は、ユーザコードではない`withPrintWriter`が最後にファイルを閉じることを保証していることである
- ファイルをクローズし忘れることが不可能である
- このテクニックはローンパターンと呼ばれる。なぜなら、`withPrintWriter`のような制御抽象概念の関数は、リソースをオープンして関数に貸し出すからである
- 例えば、前の例の`withPrintWriter`では`PrintWriter`を`op`関数に貸し出している
- その関数が終わった時、借りていたリソースがないことを意味する
- リソースは、実際に確実にクローズする為に、正常に終了したか例外が発生したかに関わらず`finally`ブロックでクローズされる
- より組み込みの制御構造のように見えるようにする一つの方法は、引数のリストを囲むのを丸括弧にする代わりにカーリー括弧を使うことである
- Scalaでは引数がひとつのメソッドの起動で、丸括弧の代わりにカーリー括弧を使うことができる、例えば以下のように書ける

```scala
 scala> println("Hello, world!")
  Hello, world!
```

```scala
  scala> println { "Hello, world!" }
  Hello, world!
```

- 2つめの例は、`println`の引数を囲む括弧の代わりにカーリー括弧を使用した
- しかしながら、このカーリー括弧のテクニックは引数が一つの時だけ有効である
- ルールに違反した場合は以下のようになる

```scala
  scala> val g = "Hello, world!"
  g: java.lang.String = Hello, world!

  scala> g.substring { 7, 9 }
  <console>:1: error: ';' expected but ',' found.
         g.substring { 7, 9 }
```

- カーリー括弧に2つの引数を指定したので、エラーになった
- 代わりに丸括弧を使う必要がある

```scala
  scala> g.substring(7, 9)
  res12: java.lang.String = wo
```

- この丸括弧の代わりにカーリー括弧を使う目的は、クライアントプログラマがカーリー括弧の間に関数リテラルを書けるようにする為である
- これはメソッド呼び出しをより制御抽象概念と感じられるようにする
- 例として以前定義した`withPrintWriter`メソッドを取り上げる
- `withPrintWriter`の最後の形は引数を2つ取るので、カーリー括弧を使えない
- それにもかかわらず、`withPrintWriter`に渡す関数は引数リストの最後の項目なので、最初のFileの引数を引数リストから取り出すカリー化を使うことができる
- これは2つめの引数リストからローンのパラメータを引き剥がす

```scala
    def withPrintWriter(file: File)(op: PrintWriter => Unit) {
      val writer = new PrintWriter(file)
      try {
        op(writer)
      } finally {
        writer.close()
      }
    }
```

- その新しいバージョンは、古いものと比べると、引数が2つあったところを、引数が1つであるリストを2つ取るところが異なる
- 以下のようにより心地よい記法でメソッドを呼び出すことができる

```scala
  val file = new File("date.txt")

  withPrintWriter(file) {
    writer => writer.println(new java.util.Date)
  }
```

- この例では、Fileを含む最初の引数リストは、丸括弧で括られている
- 2番目の引数リストは、一つの関数値を含んでおり、カーリー括弧で括られている

# 9.5 By-name parameters

- 前章の`withPrintWriter`メソッドは、カーリー括弧の間に引数を取るという点で、`if`や`while`のような言語に組み込まれている制御構造とは異なる
- `withPrintWriter`メソッドは`PrintWriter`型の一つの引数を取る
- この引数は`writer =>`として現れる

```scala
  withPrintWriter(file) {
    writer => writer.println(new java.util.Date)
  }
```

- もし`if`や`while`に近いものを実装したいとして、カーリー括弧の間のコードに渡す値がないとしたらどうか？
- このような時の為に、Scalaは`by-name`パラメータを提供している
- 具体的な例として、`myAssert`と呼ばれるアサーションを実装したいとする
- `myAssert`関数は入力として関数値と、実行するかどうかを決めるフラグを取る
- そのフラグが設定されている場合、`myAssert`は渡された関数を実行してその結果が`true`であることを確認する
- もしフラグが設定されていない場合、`myAssert`は何もしない
- `by-name`パラメータを使わずに`myAssert`を書くと以下のようになる

```scala
  var assertionsEnabled = true

  def myAssert(predicate: () => Boolean) =
    if (assertionsEnabled && !predicate())
      throw new AssertionError
```

- 定義自体は問題ないが、少々洗練されてない

```scala
myAssert(() => 5 > 3)
```

- 関数リテラルの引数無しの括弧と`=>`を省略したいと思って以下のように書くと、エラーになる

```scala
  myAssert(5 > 3) // Won't work, because missing () =>
```

- `by-name`パラメータはまさにこの為に存在している
- `by-name`パラメータにすることで、パラメータを与える時に`() =>`ではなく`=>`と書くことができる
- 例えば、`myAssert`の条件のパラメータを`by-name`パラメータを使って`() => Boolean`を` => Boolean`と書き換えることができる

```scala
    def byNameAssert(predicate: => Boolean) =
      if (assertionsEnabled && !predicate)
        throw new AssertionError
```

- これで、assert関数の引数無しの括弧を省略することができる
- その結果、`byNameAssert`を使うと、まさにビルトインの制御構造を使ったように見える

```scala
 byNameAssert(5 > 3)
```

- `by-name`型は、引数無しの括弧()を省略することができ、それはパラメータに対してのみ使用できる
- `by-name`変数や`by-name`フィールドのようなものはない
- 今、あなたはなぜ引数に`Boolean`をとるシンプルな`myAssert`を書けないのか、と不思議に思うだろう

```scala
  def boolAssert(predicate: Boolean) =
    if (assertionsEnabled && !predicate)
      throw new AssertionError
```

- この形式はもちろん文法上正しく、この`boolAssert`を使ったコードは以前と全く同じように見える

```scala
 boolAssert(5 > 3)
```
- それにも関わらず、この2つのアプローチの違いは覚えておく必要がある
- なぜなら`boolAssert`のパラメータの型は`Boolean`で、`boolAssert(5 > 3)`の括弧の中の式は`boolAssert`が呼び出される前に評価される
- `boolAssert`に渡る時、``5 > 3`の式がtrueとなる
- 反対に、`byNameAssert`のパラメータの型は`=>`なので、`byNameAssert(5 > 3)`の括弧の内側は`byNameAssert`が呼び出される前には評価されない
- その代わりに、適用メソッドが`5 > 3`を評価する関数値が作成され、この関数値は`byNameAssert`に渡される
- それ故、この2つのアプローチの違いは、アサート機能がdisableの時にある
- `boolAssert`には、引数の括弧の内側の式に副作用があり、`byNameAssert`にはないことが分かるだろう
- 例えば、アサート機能がdisableの場合でも、`boolAssert`では"`x / 0 == 0"をチェックすると例外が発生する

```scala
  scala> var assertionsEnabled = false
  assertionsEnabled: Boolean = false

  scala> boolAssert(x / 0 == 0)
  java.lang.ArithmeticException: / by zero
           at .<init>(<console>:8)
          at .<clinit>(<console>)
          at RequestResult$.<init>(<console>:3)
          at RequestResult$.<clinit>(<console>)...
```

- しかし、同じコードでも`byNameAssert`の場合、例外が発生しない

```scala
scala> byNameAssert(x / 0 == 0)
```

# 9.6 Conclusion

- この章は、制御抽象概念を構築する為のScalaの豊富な関数サポートを構築する方法を紹介した
- コードにある一般的な制御パターンを取り除く関数を使うことができ、Scalaのライブラリにある、プログラマのコードに横断的に共通する制御パターンを再利用する為の高階関数の利点を得ることができる
- この章は、高階関数を使って短く書く為にカリー化や`by-name`パラメータの使い方も紹介した
- 前章とこの章では、関数についてとても多くのことを見てきた
- 次からの数章では言語のオブジェクト指向の機能の議論に戻る



