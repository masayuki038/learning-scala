# 序章

- Scalaは一握りの制御構造しか持たない
- 制御構造は`if`、`while`、`for`、`try`、`match`と関数呼び出しだけである
- Scalaの制御構造が少ない理由の発端は、関数リテラルを持っているからである
- 相次ぐ基本的な文法でより高いレベルの制御構造を積み上げる代わりに、Scalaはライブラリでそれらを積み上げる
- 9章ではそれがどのように実行されるか詳細に示す
- この章では、これらの少ないビルトインされている制御構造を示す
- Scalaの制御構造は何かしらの値を返すことに気が付くだろう
- これは関数型言語のアプローチで、プログラムは値を計算するものと見なされ、それ故コンポーネントもまた値を計算するべきである
- この論理制御構造のアプローチは最近の命令プログラミング言語にも採用されている
- 命令プログラミング言語は、関数呼び出しは引数で与えられた値で出力内容を更新するが、関数呼び出しは値を返すこともできる
- 加えて、命令プログラミングはしばしば、`if`と同じ振る舞いをする(但し値を返す)三項演算子を持つ
- Scalaはこの三項演算子モデルを採用したが、これを`if`としている
- 言い換えると、Scalaの`if`は値を返す
- Scalaはこの方針に則り、`for`、`try`、`match`もまた値を返す
- プログラマは関数の戻り値を使うのと同じように、これらの結果値を使用してコードを単純化することができる
- この仕組みが無いと、プログラマは制御構造で計算された値を保持する為に一時変数を作成する必要がある
- 一時変数の利用を除去することは、コードをシンプルにし、値を設定し忘れるようなバグを防ぐ
- 全体として、Scalaの基本構造は、ミニマルであるが、命令プログラミング言語の本質的な部分をすべて備えている
- さらに、結果値を一貫して取得することで、コードを短くすることができる
- どのように動作するかを説明するために、この章ではScalaの基本的な制御構造を詳しく見ていく

# 7.1 If expressions

- Scalaの`if`は他の言語と同様、条件をテストして、その結果がtrueかどうかで2つのブランチのひとつを実行する
- これは命令プログラミングスタイルの一般的な例である

```scala
var filename = "default.txt"
if (!args.isEmpty)
  filename = args(0)
```

- このコードは`filename`という変数を宣言し、デフォルトの値を初期化する
- その後、引数が1つでもあるかをチェックする為に`if`式を使う
- もしそうであれば、変数に設定されている値を変更する
- Scalaの`if`は結果を返すので、このコードはもっとうまく書くことができる
- 以下のコードはvarを使わず前の例と同じ結果になる

```scala
val filename =
  if (!args.isEmpty) args(0)
  else "default.txt"
```

- この時、`if`は2つの分岐を持つ
- `args`が空でない時、初期値は`args(0)`になる
- そうでなければ、デフォルトの値になる
- `if`式に結果が返ると、`filename`変数はその値で初期化される
- このコードはちょっと短くなったが、実際の利点はvarの代わりにvalを使っていることだ
- valを使うのは関数型のスタイルっで、Javaのfinalの変数と同じように役に立つ
- コードの読み手に、変数の値が決して変わることがなく、値の変更箇所を調べる為にすべてのコードを見る必要がない
- 2つ目の利点は、varの代わりにvalを使うと、等式推論がサポートされ、より良くなる
- 導入された変数は、式に副作用がないと仮定して、それを計算する式に等しい
- これは、変数名を書く時はいつでも、式を書く代わりとできる
- 例として、`println(filename)`の代わりに、以下のように書くことができる`

```scala
println(if (!args.isEmpty) args(0) else "default.txt")
```

- この選択はあなたのものだ
- もう一方の方法で書くことができる
- valを使うことは、時間とともにコードが改善されていくように安全にリファクタリングできるようにする

# 7.2 While loops

- Scalaの`while`は他の言語と同様に、条件とボディがあり、条件結果が`true`の間はボディを何度も実行する

```scala
    def gcdLoop(x: Long, y: Long): Long = {
      var a = x
      var b = y
      while (a != 0) {
        val temp = a
        a = b % a
        b = temp
      }
      b
    }
```

- Scalaは`do-while`もある。これはボディを実行した前ではなく後に条件をテストする

```scala
    var line = ""
    do {
      line = readLine()
      println("Read: "+ line)
    } while (line != "")
```

- `while`と`do-while`は"loops"と呼ばれる構文で、式ではない、なぜなら、それらは値を返さない
- 結果の型は`Unit`となる
- `Unit`という型を持つ値が存在していることがわかる
- それは`unit value`と呼ばれ、`()`と記述される
- `()`の存在は、Javaの`void`とは異なる

```scala
  scala> def greet() { println("hi") }
  greet: ()Unit

  scala> greet() == ()
  hi
  res0: Boolean = true
```

- ボディの前に`=`が無いので、`greet`は`Unit`の結果を産み出す
- それゆえ、`greet`は`()`というunitの値を返す
- それは次の行で、`greet`の結果と`()`を比較して`true`が返ってきていることで確かめることができる
- これに関連して、unitの値の結果を返すもう一つの別の構文は、varに再代入することだ
- 例えば、一行ずつ読んでいくような以下のコードをJavaから持ってきてScalaで書いたとする

```scala
  var line = ""
  while ((line = readLine()) != "") // This doesn't work!
    println("Read: "+ line)
```

- このコードをコンパイルすると、ScalaはunitとStringを`!=`で比較すると常にtrueが返る、というワーニングを出す
- Javaでは標準入力からの一行をlineに代入するが、Scalaでは代入は常ににtrueを返す
- それゆえ、`line = readLine()`は常にtrueを返し、""になることはない
- 結果として、無限ループになってしまう
- while loopは結果を返さないので、純粋な関数言語ではしばしば除外される
- それらの言語はloopではなく式を持つ
- Scalaはそれでもwhile loopを持っている、なぜなら、特に命令形プログラミングをやってきたプログラマにとって、命令形はより読みやすい
- 再帰を使わずwhile loopで表現することにより、読みやすくなる
- 例えば、gcdメソッドは以下のように再帰を用いて記述することで、varが不要になる

```scala
    def gcd(x: Long, y: Long): Long =
      if (y == 0) x else gcd(y, x % y)
```

- 一般的には、varと同じ方法で、コード中のwhile loopにも同じように挑戦することをオススメする
- 実際、while loopとvarは密接な関係になることが多い
- while loopは値を返さないので、通常はvarを更新したりI/Oまわりの処理を行う

# 7.3 For expressions

- Scalaの`for`は、イテレーションの幅広いバリエーションを表現する為に、いくつかのシンプルな構成要素を異なる方法で結びつける
- シンプルな利用は連続した整数をイテレーションするような一般的なタスクを可能にする
- より進んだ式は複数種の異なったコレクションにまたがってイテレーションできたり、任意の条件に基づいた要素をフィルタしたり、新しいコレクションを生成する

## Iteration through collections

- `for`ができるもっともシンプルなことは、コレクションのすべての要素を走査することだ
- 以下の例はカレントディレクトリのすべてのファイルを出力するコードである

```scala
    val filesHere = (new java.io.File(".")).listFiles

    for (file <- filesHere)
      println(file)
```

- `file <- filesHere`の部分はジェネレータと呼ばれ、`filesHere`の要素を走査する
- それぞれのイテレーションでは、`file`という新しいval変数が要素の値で初期化される
- その`for`式の文法は、配列でなくてもどんなコレクションでも動く
- とても便利な例は、以下のようなRange型のものだ

```
  scala> for (i <- 1 to 4)
          println("Iteration "+ i)
  Iteration 1
  Iteration 2
  Iteration 3
  Iteration 4
```

- もし上限値の「4」を含みたくない場合は、以下のように`until`を使う

```
  scala> for (i <- 1 until 4)
          println("Iteration "+ i)
  Iteration 1
  Iteration 2
  Iteration 3
```

- このように整数の配列を走査する場合、以下のように配列を走査するを使うだろう

```scala
  // Not common in Scala...
  for (i <- 0 to filesHere.length - 1)
    println(filesHere(i))
```

- Scalaでこの手の方法が一般的ではないのは、Scalaはコレクションを直接走査できるからである
- もしそうすると、コードは短くなり、配列を走査するする際に発生する可能性のある、off-by-oneエラー(境界条件の判定に関するエラー)を回避することができる

## Filtering

- コレクション全体をイテレーションしたくない時、コレクションをフィルタすることができる
- 括弧内に`if`文を入れたfilterを追加したfor文で実現できる
- 例えば、カレントディレクトリの".scala"という拡張子を持つファイルのみを対象にする場合は以下のようになる

```scala
    val filesHere = (new java.io.File(".")).listFiles

    for (file <- filesHere if file.getName.endsWith(".scala"))
      println(file)
```

- 以下のコードでも、前記のコードと同じ結果になる

```scala
  for (file <- filesHere)
    if (file.getName.endsWith(".scala"))
      println(file)
```

- このコードでも、前記のコードと同じ結果になるが、命令プログラミングに馴染んだプログラマにとってはより親しみがあるコードになる
- しかしながら、この命令型の形は、1つのオプションでしかない、なぜなら、この特殊な`for`は副作用のある出力を実行し、unitの値()を返す
- このセクションの後段で説明するが、`for`式は値、`<- clauses`によって決められる型を持つコレクションを返すので、「式」と呼ばれる
- `if`を増やすことによって、フィルタを増やすことができる
- 例えば、ディレクトリではなくファイルのみを出力したい場合は以下のようになる

```
    for (
      file <- filesHere
      if file.isFile;
      if file.getName.endsWith(".scala")
    ) println(file)
```

## Nested iteration

- 複数の`<-`を設定すると、ネストしたループになる
- 例えば、以下の例は2つのネストしたループになっている
- 外側のイテレーションは".scala"で終わる`filesHere`で、内側のイテレーションは`fileLines`となる。

```scala
    def fileLines(file: java.io.File) =
      scala.io.Source.fromFile(file).getLines.toList

    def grep(pattern: String) =
      for (
        file <- filesHere
        if file.getName.endsWith(".scala");
        line <- fileLines(file)
        if line.trim.matches(pattern)
      ) println(file +": "+ line.trim)

    grep(".*gcd.*")
```

- お好みで、ジェネレータやフィルタの周りを丸括弧で囲む変わりにカーリー括弧を使うことができる
- カーリー括弧を使う一つの利点は、丸括弧を使った時に必要なセミコロンを省略することができる

# Mid-stream variable bindings

- 前のコードは、`line.trim`を繰り返し実行している
- これは全体的な処理なので、一度だけ処理するようにしたい
- これは等号(=)を使って新しい変数に結果をバインドすることによって可能である
- 新しく導入した変数は`val`のように扱われる

```scala
    def grep(pattern: String) =
      for {
        file <- filesHere
        if file.getName.endsWith(".scala")
        line <- fileLines(file)
        trimmed = line.trim
        if trimmed.matches(pattern)
      } println(file +": "+ trimmed)

    grep(".*gcd.*")
```

- 上記コードでは、`trimmed`が`for`式の途中で導入されている
- その変数は`line.trim`の結果で初期化されている
- それから`for`式の残りは2箇所で新しい変数を使う、一つは`if`のところで、もう一つは`println`の中で

## Producing a new collection

- これまでの例では、値をイテレートさせて、それらを捨てていた
- それぞれのイテレーションで後段で使う値を生成することもできる
- そうする為に、`yield`というキーワードを式のボディの前に配置することができる
- 例えば、これは.scalaファイルを識別して配列に入れる関数である

```scala
  def scalaFiles =
    for {
      file <- filesHere
      if file.getName.endsWith(".scala")
    } yield file
```

- 式のボディは都度都度一つの値を生成する、このケースでは単にファイルである
- 式の実行が完了すると、結果は一つのコレクションに生成した値がすべて含まれる
- この結果のコレクションの型はイテレーション節で処理されるコレクションの型に基づく
- このケースでは、`filesHere`が配列で、生成された型はファイルなので、結果はArray[File]となる
- `yield`の配置場所には注意する必要がある。文法としては`for clauses yield body`となる
- このyieldはボディの前に置く必要がある
- もしボディがカーリー括弧に囲まれているようなら、`yield`は最初のカーリー括弧の前に配置する必要がある
- 例えば、以下の例では、最初はカレントディレクトリの全ての.scalaファイルを`filesHere`という名前のArray[File]に変換する
- これらの各々は`Iterator[String]`を生成する
- この最初のイテレータは、トリムされ、行中に`for`がある行のみを内包するもう一つのIterator[String]に変換する
- 最終的に、それぞれで行の長さを生成する
- この式の結果は、それぞれの行の長さが格納されたArray[Int]となる

```
    val forLineLengths =
      for {
        file <- filesHere
        if file.getName.endsWith(".scala")
        line <- fileLines(file)
        trimmed = line.trim
        if trimmed.matches(".*for.*")
      } yield trimmed.length
```

# 単語

- handful: 一握り、少量、少数
- inception: 初め、発端
- after another: 相次ぎ
- accumulate: 蓄積する、積もる、堆積する
- sufficient: 十分な、足りる
- turn out: 結果的に～であることがわかる、～という状態で終わる、～になる
- construct: 構文
- relevant: 関係のある、現実の問題に直結する
- nonetheless: それにもかかわらず
- ingredient: 構成要素、成分、原料
- combine: ～を混ぜ合わせる、～を結び付ける、兼ね備える
- arbitrary: 気まぐれな、任意の、偶然による
- entirety: 完全であること、全部、全体