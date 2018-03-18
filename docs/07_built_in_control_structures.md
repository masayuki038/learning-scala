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

# 7.4 Exception handling with try expressions

- Scalaの例外は他の多くの言語と同様の振る舞いを実行する
- 通常の値の返却の代わりに、メソッドは例外を投げることによって終了する
- メソッドの呼び出し側は例外をキャッチしてハンドリングするか、あるいはさらに呼び出し側に例外を伝播して終える
- その例外は、このような方法でコールスタックを遡っていきながら、その例外がハンドリングされるか、メソッドが無くなるまで伝播していく

## Throwing exceptions

- 例外をスローするのはJavaと同じである
- 例外オブジェクトを作成して`throw`キーワードでスローする
- 少し奇妙に見えるかもしれないが、Scalaでは、`throw`は結果の型を持つ式である

```scala
  val half =
    if (n % 2 == 0)
      n / 2
    else
      throw new RuntimeException("n must be even")
```

- `n`が偶数の場合は`half`は`n`の半分の値になる
- `n`が偶数ではない場合は、`half`は全く初期化されず例外がスローされる
- これにより、どんな値の種類でもスローされた例外を安全に取り扱うこができる
- スローからreturnを使用しようとするコンテキストでも、決してそうすることはない、それゆえ、無害である
- 技術的には、例外のスローはNothing型である
- 実際には何も評価することはないけれども、式としてスローを使うことはできる
- ちょっとした技術的な動きは奇妙に見えるかもしれない
- しかし、前例のようにしばしば使える
- 一方で値を計算し、もう一方で例外をスローして計算しない
- すべての`if`式の型は計算する側の型となる
- Nothing型については後程11.3章で説明する

## Catching exceptions

- 以下のようなコードで例外をキャッチすることができる
- catch節はパターンマッチングで一致したものが選ばれる

```scala
    import java.io.FileReader
    import java.io.FileNotFoundException
    import java.io.IOException

    try {
      val f = new FileReader("input.txt")
      // Use and close file
    } catch {
      case ex: FileNotFoundException => // Handle missing file
      case ex: IOException => // Handle other I/O error
    }
```

- try-catch式の振る舞いは他の言語と同じである
- ボディを実行して、もし例外をスローすると、各々のcatch節が次々に試される
- この例では、例外の型がFileNotFoundExceptionの場合、最初の節が実行される
- 例外の型がIOExceptionの場合、二番目の節が実行される
- 例外の型がどれにも該当しない場合は、try-catchは終了してより上位に伝播する
- Note: Javaと違って、Scalaには宣言例外がない
- 例外を宣言したい場合は、`@throw`アノテーションをつける


## The finally clause

- (正常に終わったのか例外をcatchしたのか上位にスローしたのか)終わり方を気にせず実行したいコードがある場合には、finally節で囲むことができる
- 例えば、メソッドが例外をスローして終わったかどうかにかかわらずオープンしたファイルをクローズしたい場合は以下のように記載する

```scala
    import java.io.FileReader

    val file = new FileReader("input.txt")
    try {
      // Use the file
    } finally {
      file.close()  // Be sure to close the file
    }
```

- Note: 上記のコードはファイルやソケット、データベースの接続のようなノンメモリリソースを確保するための慣用的な表現である
- 最初にリソースを求める
- リソースを使い始める場所でtryブロックを開始する
- 最後にfinallyブロックでリソースをクローズする
- このイデオムはJavaでもScalaでも同じであるが、Scalaはより短いコードで同じ効果を持つloan patternというと呼ばれるテクニックを採用することもできる
- loan patternについては9.4章で説明する

## Yielding a value

- Scalaのほとんどの制御構造と同様に、`try-catch-finally`は値を返す
- 例えば、以下のコードはURLのparseをtryの中で行っているが、URLがマズい形式の時にはdefaultの値が返るようになっている
- その結果は、例外が発生しない時のtry節の結果か、例外がスローされてキャッチしたcatch節の結果である
- もし例外がスローされてキャッチされなかった場合は、式は全く結果を返さない
- finally節で計算される値は、もしあれば、捨てられる
- 通常、finally節はファイルをクローズするようなクリーンアップ系の処理を行う
- それらは通常メイン処理やキャッチ節で計算する値を変更するべきではない

```scala
    import java.net.URL
    import java.net.MalformedURLException

    def urlFor(path: String) =
      try {
        new URL(path)
      } catch {
        case e: MalformedURLException =>
          new URL("http://www.scala-lang.org")
      }
```

- Javaに慣れ親しんでいるとして、Javaが`try-finally`で値を返さないからといってScalaがJavaと振る舞いが異なるのは問題ではない
- Javaでは明示的なreturnステートメントを含むfinally節や例外をスローするのは、その前に`try-catch`で発生したことを覆い隠す

```scala
  def f(): Int = try { return 1 } finally { return 2 }
```

- `f()`は2を返す

```
  def g(): Int = try { 1 } finally { 2 }
```

- 対照的に`g()`は1を返す
- これらの関数の振る舞いは多くのプログラマを驚かせる
- それゆえ、通常、finally節では値を返すのを避けるべき
- finally節の最も良い考え方は、オープンしていたファイルをクローズするような副作用を確実にすることである

# 7.5 Match expressions

- Scalaのパターンマッチは他の言語のswitch節のように複数の選択肢から一つ選択させる
- 一般的に`match`式は15章で説明するように適切なパターンを指定することができる
- 一般的な形式は待つことができる
- 今は、複数の選択肢から`match`節を使って一つ選択することについて考える
- プログラム引数から食べ物の名前を読み、それに関連する食べ物の名前を出力するスクリプトの例は以下のとおり
- この`match`節はプログラム引数の最初の値を見ている
- それが`salt`であれば`pepper`と、`chips`であれば`salsa`と表示する
- アンダースコア(_)で受けているそれ以外のケースは、Scalaでは値の分からないプレースホルダとして使用される

```scala
    val firstArg = if (args.length > 0) args(0) else ""

    firstArg match {
      case "salt" => println("pepper")
      case "chips" => println("salsa")
      case "eggs" => println("bacon")
      case _ => println("huh?")
    }
```

- Javaの`switch`節と比べると重要な違いがいくつかある
- ひとつは、Javaのcase節はinteger型かenum型しか使えないのとは異なり、他と同様、Scalaではどんな種別の定数でも使うことができる
- もうひとつの違いは、それぞれの選択肢の最後にbreakを付けていない
- breakが暗黙的である代わりに、ある選択肢が選ばれたら他の選択肢は選ばれない(フォールスルーされない)
- それにより短く書けるようになり、プログラマが不意にフォールスルーしてしまうことが無いのでエラーを回避できる
- しかしながら、Javaのswitchとの最も重要な違いは、結果値の`match`式だろう
- 先の例では、`match`式のそれぞれの選択肢は、値を出力する
- 以下の例は、値を表示するよりもむしろ値を生成している
- `match`式の結果の値は`friend`変数に格納される
- コードが短くなることを除いて、そのコードは2つの異なる関心事に分離される、一つ目は食べ物を選ぶこと、そして表示すること

# 7.6 Living without break and continue

- breakやcontinueについて言及してこなかったことに気づくだろう
- 次の章で説明するように、Scalaではそれらは関数リテラルに調和しないので、これらの命令を省略する
- `continue`がwhileループの中で何を意味するかは明確ですが、関数リテラル内ではどういう意味になるだろう？
- Scalaは命令型と関数型の両方のプログラミングスタイルをサポートするが、このケースでは言語をシンプルにする代わりに、関数型プログラミングに少し偏っている
- けれども心配する必要はない。`break`や`continue`を使わないプログラミングの方法はたくさんある
- そしてもし関数リテラルの利点を取りたいなら、これらの代替はしばしば元のコードを短くする
- 最もシンプルなアプローチは、すべての`continue`をifに、`break`をbooleanに変更することだ
- booleanの値はwhile loopを続けるべきかどうかを示す
- 例えば、「.scala」で終わっていて且つ「-」で始まらない引数を探すことを考える
- Javaで`break`と`continue`を使って書くと以下のようになる

```java
  int i = 0;                // This is Java
  boolean foundIt = false;
  while (i < args.length) {
    if (args[i].startsWith("-")) {
      i = i + 1;
      continue;
    }
    if (args[i].endsWith(".scala")) {
      foundIt = true;
      break;
    }
    i = i + 1;
  }
```

- このJavaコードをScalaに直接書き直す為に、if～continueの代わりにwhile loopのボディをifで囲む
- breakを取り除く為に、通常はループを続けるかどうかを示すbooleanを追加するが、このケースではfoundItを再利用する
- これらのトリックを使って、コードを整理すると以下のようになる

```scala
    var i = 0
    var foundIt = false

    while (i < args.length && !foundIt) {
      if (!args(i).startsWith("-")) {
        if (args(i).endsWith(".scala"))
          foundIt = true
      }
      i = i + 1
    }
```

- 上記のScalaコードは、元のJavaコードにかなり似ている
- すべての基本的な要素は同じ順序でそこに配置されている
- 2つの再割り当て可能な変数とwhileループがある
- 内側のループは、引数リストの長さよりもiが小さいかどうかと、".scala"かどうかをチェックしている
- もし上記コードからvarsを取り除きたいのであれば、ひとつのアプローチとしてループを再起呼び出しに書き換える
- 例えば、整数を取るsearchForm関数は指定された位置から始め、求めている値のインデックスを返す
- このテクニックを使うと以下のようになる

```scala
    def searchFrom(i: Int): Int =
      if (i >= args.length) -1
      else if (args(i).startsWith("-")) searchFrom(i + 1)
      else if (args(i).endsWith(".scala")) i
      else searchFrom(i + 1)

    val i = searchFrom(0)
```

- 上記コードはその関数名に読みやすい名前をつけ、そしてループの部分は再起呼び出しになっている
- おのおのの`continue`は引数として`i + 1`を指定する再起呼び出しに置き換えられる
- 多くの人たちは、一度再起呼び出しを理解すると、このコードスタイルを理解しやすい
- Note: Scalaコンパイラは上記コードを実際には再起呼び出しにしないだろう
- 再起呼び出しのすべては関数の末尾で呼び出されているので、コンパイラはwhileループに近いコードを生成する
- それぞれの再起呼び出しは関数の最初に戻るように実装されるだろう
- 末尾再起呼び出しの最適化については、8.9章で説明する

# 7.7 Variable scope

- このセクションでは制御構造を使ってScalaでscopeがどのように働くか説明する

## Fast track for Java programmers

- あなたがJavaプログラマなら、ScalaのスコープのルールはほとんどJavaと同じである
- JavaとScalaにひとつ違いがある、それはScalaはネストしたスコープに同じ変数名を使うことができる
- それゆえ、あなたがJavaプログラマなら、このセクションを少なくとも1回はざっと見てほしい
- Scalaプログラムの変数定義は、その名前を使うことができるスコープを定義する
- スコープのもっとも一般的な例は、通常カーリー括弧が新しいスコープを導入するということだ
- カーリー括弧の内側で定義されたものは何であれ、カーリー閉じ括弧の後にはスコープ外になる

```scala
    def printMultiTable() {

      var i = 1
      // only i in scope here

      while (i <= 10) {

        var j = 1
        // both i and j in scope here

        while (j <= 10) {

          val prod = (i * j).toString
          // i, j, and prod in scope here

          var k = prod.length
          // i, j, prod, and k in scope here

          while (k < 4) {
            print(" ")
            k += 1
          }

          print(prod)
          j += 1
        }

        // i and j still in scope; prod and k out of scope

        println()
        i += 1
      }

      // i still in scope; j, prod, and k out of scope
    }
```

- `printMultiFunction`は九九を出力する
- この関数の最初のステートメントは`i`という名前の変数を導入して整数の値1で初期化する
- `i`という名前はその関数内で使うことができる
- `printMultiTable`の次のステートメントはwhile loopである

```scala
  while (i <= 10) {

    var j = 1
    ...
  }
```

- `i`はスコープ内なので、ここで使うことができる
- while loopの最初のステートメントは、jという名前の変数を定義し、また整数1で初期化する
- jはwhile loopのカーリー括弧の内側で定義されたので、while loopの中でしか使えない
- もしwhile loopのカーリー閉じ括弧の後に`j`で何かしようとすると、その後のコメントにあるように`j`と`prod`と`k`はスコープ外になり、コンパイルできない
- この例で定義されているすべての変数`i`、`j`、`prod`、`k`はローカル変数である
- 変数が実行される度に、新しいローカル変数のセットが使用される
- 一度変数が定義されると、同じスコープ内では同じ名前を使うことができない

```scala
  val a = 1
  val a = 2 // Does not compile
  println(a)
```

- 一方で、外のスコープの変数と同じ名前を持つ変数を内側のスコープで定義することはできる

```scala
  val a = 1;
  {
    val a = 2 // Compiles just fine
    println(a)
  }
  println(a)
```

- 上記のコードが実行されると、まず2が表示され、その後1が表示される
- なぜなら、カーリー括弧の内側で定義されたものはカーリー閉じ括弧までの間の、異なる変数スコープだから
- ScalaとJavaで覚えておくべき違いはこの一点で、Javaは外側のスコープで定義された変数名を内側のスコープで定義することができない
- Scalaプログラムにおいては、内側の変数は外側の変数をシャドウする、と言われている
- なぜなら、外側の変数は内側のスコープでは見えなくなるから
- インタープリタでは、以下のようにシャドウされる

```scala
  scala> val a = 1
  a: Int = 1

  scala> val a = 2
  a: Int = 2

  scala> println(a)
  2
```

- インタープリタでは、変数名の再利用ができる
- 中でも、インタープリタで初めて変数を定義した時に間違えた場合、これを変更することができる
- これが可能な理由は、インタープリタはそれぞれの新しいステートメントで新しいネストしたスコープを作成するからである
- それゆえ、先程の例は以下のようになる

```scala
  val a = 1;
  {
    val a = 2;
    {
      println(a)
    }
  }
```

- このコードをコンパイルしてScalaスクリプトとして実行したり、インタープリタにコードをタイプすると、2が出力される
- このようなコードは、コードの読み手はとても混乱することを覚えておいてほしい
- なぜなら、ネストされたスコープでは変数名は新しい意味を持つからである
- 普通は、外側のスコープの変数をシャドウするよりも、新しい意味のある名前を選ぶ方が良い

# 7.8 Refactoring imperative-style code

- 関数型言語の洞察を得る為に、このセクションでは前回の九九を表示する命令言語のアプローチで書かれたコードをリファクタリングする

```scala
    // Returns a row as a sequence
    def makeRowSeq(row: Int) =
      for (col <- 1 to 10) yield {
        val prod = (row * col).toString
        val padding = " " * (4 - prod.length)
        padding + prod
      }

    // Returns a row as a string
    def makeRow(row: Int) = makeRowSeq(row).mkString

    // Returns table as a string with one row per line
    def multiTable() = {

      val tableSeq = // a sequence of row strings
        for (row <- 1 to 10)
        yield makeRow(row)

      tableSeq.mkString("\n")
    }
```

- 命令言語のスタイルを2つの方法で明らかにする
- 1つ目は、`printMultiTable`は九九を標準出力に表示する、という副作用がある
- なので、九九が文字列で返ってくるように関数を修正した
- 関数は全く表示しないので、`multiTable`という名前に変更した
- 前述のように、副作用のない関数の利点はユニットテストをしやすいことである
- `printMultiTable`をテストする為には、出力内容が正しいかどうかを確認する為に`print`や`println`を少し再定義する必要がある
- `multiTable`をテストするのはもっと簡単で、結果として返ってくる文字列をチェックすれば良い
- `printMultiTable`の命令言語スタイルの他の予兆は、while loopとvarsである
- 対照的に、`multiTable`はvals、for式、helper関数を使い、`mkString`を呼び出している
- コードをより読みやすくする為に、`makeRow`と`makeRowSeq`という2つのhelper関数を作成した
- `makeRowSeq`関数は1から10までの列番号でループするfor式を使う
- for文の内容は行と列の計算を行い、結果と結果の間を埋めるスペースを決め、そのスペースと結果を連結して文字列を生成する
- for式の結果はそれぞれの要素が文字列として生成されたものを含むシーケンスになる
- その他のヘルパー関数は`makeRow`で、`makeRowSeq`の戻り値を指定して`mkString`を実行するだけである
- `mkString`はそのシーケンスを連結し、一つの文字列として返す
- `multiTable`メソッドは最初に1～10までの行番号でループしたfor式の結果で`tableSeq`を初期化し、それぞれのforは行の文字列を取得する為に`makeRow`を呼び出す
- そしてこのfor式で文字列が生成され、このfor式の結果は行の文字列のシーケンスとなる
- 残りのタスクは文字列のシーケンスを一つの文字列に変換するだけだ
- `mkString`に"\n"を渡して呼び出してそれぞれの文字列の間に改行を入れた一つの文字列を生成して完成

# 7.9 Conclusion

- Scalaのビルトインの制御構造は小さいが、しっかり仕事をする
- それらの振る舞いは命令言語と非常に良く似た働きをするが、価値をもたらす傾向がある為、関数的なスタイルもサポートする

# 単語