# 序章
- Scalaの本格的なチュートリアルを始める前に、Scalaの雰囲気を味わう為に2つの章を用意した
- そして重要なことは、コードを書くことだ
- この章のすべてのコードを試してから次に進むことを推奨する
- Scalaを学び始める一番良い方法は、Scalaのプログラムを書くことだ
- ベテランのあなたがScalaに入門するのであれば、次の章はScalaで有用なコードを書き始めることができるようになる為に十分であろう
- 経験の少ないあなたには、少し不思議に見えるかもしれない
- 心配はいらない。素早く学ぶ為に、詳細を省いている
- すべては"消化ホース"のスタイルで後段の章で説明される
- 我々は次の2つの章に、より詳細な説明がある本のセクションを示した注釈を入れた

# Step 1. Learn to use the Scala interpreter
- Scalaを始める最も簡単な方法は、Scalaの記法やプログラムを書くためのシェルであるScalaインタプリタを開始することだ
- インタプリタにコードを書くと、評価されて結果が表示される
- このシェルは単にscalaと呼ばれる
- '1+2'のようにタイプしてエンターキーを押すと、

```
scala> 1 + 2
```

- インタープリタは以下のように出力する

```scala
res0: Int = 3
```

- これは以下のことを示す
  - 計算された結果を保存する変数は自動的に生成されるかユーザが定義できる(res0はResult 0の意味)
  - コロン(:)の後に型の名前が続く(Int)
  - イコール記号(=)
  - 値は計算式が評価された結果である

- Int型はscalaパッケージのIntクラスの名前である
- ScalaのパッケージはJavaのパッケージに似ている
- グローバルな名前空間のパーティションで、情報隠蔽(フルパッケージ名を記載しないと意味で)の仕組みを提供する
- Intクラスの値はJavaのintの値に対応する
- もう少し広げて言うと、Javaのすべてのプリミティブ型はScala側のクラスに対応がある
- 例えばScalaのBooleanはJavaのBooleanに、scalaのFloatはJavaのfloatに対応する
- さらに、ScalaのコードがJavaのバイトコードに変換される際、Scalaのコンパイラはプリミティブ型のパフォーマンスの恩恵を得る為に、Javaのプリミティブ型を使う
- resXは以下のように利用できる。例えば、前回res0には3が設定されているので、res0 * 3は9になる

```scala
scala> res0 * 3
res1: Int = 9
```

- 出力する必要があれば、"Hello, world! greeting"とタイプすれば良い

```scala
scala> println("Hello, world!")
Hello, world!
```

println関数は、JavaのSystem.out.printlnに似ていて、標準出力に文字列を出力する

# Step 2. Define some variables
- Scalaの変数には2種類ある
- 一つはJavaのfinalの変数と同様に値の再割り当てできないval
- もう一つはJavaのfinal無し変数と同様に値の再割り当てができるvar

```scala
scala> val msg = "Hello, world!"
msg: String = Hello, world!
```

- このステートメントはmsgという名前の変数に"Hello, world!"という文字列を割り当てる
- Scalaの文字列の実装はJavaのStringクラスなので、msgの型はjava.lang.Stringである
- Javaで変数宣言をしたことがあれば、val宣言のどこにもjava.lang.StringやStringがない、という違いに気が付くだろう
- この型推論の例が示すとおり、Scalaは型の指定を省略することができる
- このケースでは、文字列リテラルでmsgを初期化したので、Scalaはmsgの型がStringであると推測した
- Scalaが型を推測する時、不要に型を明記するよりもベストな選択をする
- しかしながら、型を明記することもできる
- 型の明記はScalaのコンパイラはあなたの意図の型を推測するだけでなく、将来コードを読む人にとっての有用なドキュメントになる
- Javaとは対照的に、Scalaは変数名の後にコロンで区切って型を指定する

```scala
scala> val msg2: java.lang.String = "Hello again, world!"
msg2: String = Hello again, world!
```

- あるいは、java.langの型はScalaプログラムでは省略できる

```scala
scala> val msg3: String = "Hello yet again, world!"
msg3: String = Hello yet again, world!
```

- 最初のmsgに戻り、定義した変数は適宜使うことができる

```scala
scala> print(msg)
Hello, world!
```

- msgはvalで宣言したので、値を再定義できない

```scala
scala> msg = "Goodby cruel world"
<console>:11: error: reassignment to val
       msg = "Goodby cruel world"
```

- varで宣言すれば値を再定義できる

```scala
scala> var greeting = "Hello, world!"
greeting: String = Hello, world!

scala> greeting = "Leave me alone, world!"
greeting: String = Leave me alone, world!
```

- 複数行を入力したい場合は、最初の行の後に続けてタイプする
- もしコードの入力が終わってなければ、インタープリタは次の行に|を表示する

```scala
scala> val multiline =
     | "This is the next line."
multiline: String = This is the next line.
```

- あなたの入力に何か間違いがあっても、enterが2回押されるまでは、インタープリタはさらに入力を待ち続ける

```scala
scala> val oops =
     |
     |
You typed two blank lines.  Starting a new command.
```

- 本書では以降、コードを読みやすくする為に|は省略する(インタープリタにコピペしやすいように)

# Step 3. Define some functions
- 変数に関して一通り終わったので、いくつか関数を書きたいと思うだろう
- Scalaの関数は以下のようになっている

```scala
def max(x: Int, y: Int): Int = {
           if (x > y) x
           else y
         }
```

- 関数はdefで始める
- 関数名(この場合はmax)は括弧の中にコンマで区切られた引数のリストが続いている
- コロンに続く型の記述は、すべての関数の引数で必要となる。何故なら、Scalaコンパイラは関数のパラメータの型を推論できないからである
- この例では、maxという名前の関数はxとyという2つの引数を取り、2つともInt型である
- maxの引数リストの閉じ括弧の後に、もう一つの": Int"という型記述がある
- これはmax関数の戻り値の型である
- 戻り値の型に続くのは等号と関数の本体を含むブレース括弧の対である
- この場合、関数の本体には、xとyのどちらが大きいかを返すifが1つある
- ここでひとつ明らかにすると、ScalaのifはJavaの三項演算子のように結果を返す
- 例えば、Scalaの"if (x > y) x else y"は、Javaの"(x > y) ? x : y"のように振る舞う
- 関数の本体の前にある等号は、関数プログラミングの観点から、関数は値を返す表現を定義する、という意味をほのめかす
- 時々Scalaコンパイラは関数の戻り値の型を明記させる
- 例えばもし関数が再帰的であれば、戻り値の型を明記する必要がある
- しかしながらこのmaxの場合は、省略してもコンパイラが推論する
- また、関数の本体が一文だけであれば、カーリー括弧も省略することができる
- それゆえ、maxの別の書き方は以下のようになる

```scala
scala> def max2(x: Int, y: Int) = if (x > y) x else y
max2: (x: Int, y: Int)Int
```

- 一度定義した関数は、名前で呼び出すことができる

```scala
scala> max(3, 5)
res0: Int = 5
```

- これは、関数が引数を取らず、値を返さない関数の定義である

```scala
scala> def greet() = println("Hello, world!")
greet: ()Unit
```

- greet()関数を定義した時、インタープリタは'greet: ()Unit"と返している
- greetは、もちろん、関数の名前である
- 空括弧はその関数が引数を取らないことを意味している
- そしてUnitはgreet関数の戻り値の型である
- Unitの戻り値の型は値を返さないことを意味している
- ScalaのUnitはJavaのvoidに似ており、実際、JavaのすべてのvoidあｈScalaのUnitにマッピングされる
- Unitの戻り値のメソッドは、それゆえ、副作用が発生する
- greet()の場合、副作用は標準出力にフレンドリーな挨拶が出力されることである

# Step 4. Write some Scala scripts
- Scalaは大きなシステムを構築するに向いているが、スクリプトにもうってつけである
- スクリプトはファイルに記載された順次実行されるステートメントの連続である
- このファイルをhello.scalaと名づける

```scala
println("Hello, world, from a script!")
```

- 次に実行する

```
$ scala hello.scala
```

- すると以下のようにあいさつ文が表示される
- Scalaスクリプトへののコマンドライン引数はargsという名前の配列を通して利用することができる
- Scalaにおいて、配列は0から始まり、各要素にはカッコにインデックスを指定することでアクセスする

```scala
// Say hello to the first argument
println("Hello, "+ args(0) +"!")
```

```scala
$ scala helloarg.scala planet
```

- このコマンドにおいて、"planet"はコマンドライン引数で、スクリプト内ではargs(0)に割り当てられる
- したがって、以下のように出力される

```
Hello, planet!
```

- Scalaのコンパイラは//から行末までの文字や、/*から*/の間の文字を無視する
- この例では文字列を+で連結している
- "Hello, " + "world!"は"Hello, world!"という文字列を生成する

# Step 5. Loop with while; decide with if
- printargs.scalaに以下をタイプしてwhileを試してみる

```scala
  var i = 0
  while (i < args.length) {
    println(args(i))
    i += 1
  }
```

- 注意:
  - このセクションのこの例はwhileループに関する説明であるが、ベストなScalaのデモでない
  - 次のセクションで、配列のインデックスを使わないイテレートの方法を見ることができる

- このスクリプトはi=0という変数定義でスタートする
- 型推論はiにScalaの型のIntを与える。なぜなら、それは初期値の0の型である為
- 次の行のwhileの構造は、i < args.lengthがfalseになるまで繰り返し実行される
- このブロックは2つの文を持ち、それぞれの行のインデント(2スペース)はScalaで推奨されるインデントである
- 最初の文のprintln(args(i))は、コマンドライン引数を出力する
- 次の行のi+=1は、1をインクリメントする
- Javaのi++, ++iはScalaでは機能しないこに注意
- Scalaのインクリメントはi=i+1、i+=1のどちらかである必要がある
- このスクリプトを以下のコマンドで実行する

```
  $ scala printargs.scala Scala is fun
```

- すると、以下のように出力される

```
  Scala
  is
  fun
```

- もっと面白いことをする為に、echoargs.scalaという新しいファイルに次のコードをタイプする

```scala
  var i = 0
  while (i < args.length) {
    if (i != 0)
      print(" ")
    print(args(i))
    i += 1
  }
  println()
```

- このバージョンでは、printlnの代わりにprintを呼び出す
- すると、すべての引数は一行の中に表示される
- これらを読めるようにする為に、初回のループを除き、それぞれの引数の為にスペースを1つ入れた
- while loopの中で1回目は"if (i != 0)"はfalseなので、最初の引数の前にはスペースが出力されない
- 最終的に、すべての引数を出力した後に改行を入れるもう一つのprintlnを追加したことになる
- この出力は実に素晴らしい
- このスクリプトは次のコマンドで実行する

```
$ scala echoargs.scala Scala is even more fun
```

- 以下のように出力される

```
Scala is even more fun
```

- Scalaでは、Javaと同様、whileやifのカッコの中にbooleanの表現を入れる必要がある
- 言い換えると、Rubyのようにカッコを入れず"if i < 10"と記載することはできない
- もう一つのJavaの類似点として、ブロックに1ステートメントしかない場合、echoargs.scalaと同様にカーリー括弧は省略することができる
- また、Javaとは違い、行末にセミコロンを付ない。(以下のように、付けても動くか、うるさい感じになる)

```scala
var i = 0;
while (i < args.length) {
  if (i != 0) {
    print(" ");
  }
  print(args(i));
  i += 1;
}
println();
```

# Step 6. Iterate with foreach and for
- 認識していないかもしれないが、前回のステップでloopを書いた時、命令型プログラミングのスタイルだった
- JavaやC++、Cのような命令型プログラミングのスタイルでは、一度に1つの命令を与え、それによって複数の関数間で共有されている状態を変更する
- Scalaは命令型でプログラミングできるが、Scalaをよく知るようになると、より関数型プログラミングのスタイルでプログラミングすることがよくある
- 実際、この本は命令型プログラミングのように関数型プログラミングを軽快に使えるようになることを目的としている
- 関数型言語のひとつの特徴は、関数がファーストクラスオブジェクトであることであり、Scalaもそうである
- 例えば、コマンドライン引数のそれぞれを出力するもう一つの方法は以下である

```scala
args.foreach(arg => println(arg))
```

- このコードでは、argsのforeachメソッドを呼び出し、関数を渡す
- このケースでは、argという名前の引数を取る関数リテラルを渡している
- 関数の本体は`println(arg)`である
- この前の例では、foreachを呼んでいる配列の要素の型がStringなので、Scalaインタープリタはargの型をStringと推論している
- もしより明確にしたいのであれば、引数を括弧で括って、型を明記することができる

```scala
args.foreach((args: String) -> println(arg))
```

- もしより明示的に書くかわりにより短く書きたい場合、Scalaの特別な省略記法が便利だ
- もし関数リテラルが一つの引数を取る一つのステートメントで構成される場合、メソッドの引数を指定する必要がない
- それゆえ次のコードでも動く

```scala
args.foreach(println)
```

- 纏めると、関数リテラルは括弧の中に名前のついた引数のリストがり、右矢印(=>)があって、関数の本体がある
- この点によって、JavaやC等の命令型プログラミングでは一般的なよくあるループに何が起こったか不思議に思うかもしれない
- 関数型の方向にあなたを誘う為に、Scalaでは命令型において関数型と親和性があるfor式が利用可能である

```scala
for (arg <- args)
  println(arg)
```

- forの後の括弧は`arg <- args`を含んでいる
- <-シンボルの右側は引数の配列と同じようなものである
- <-の左側のargはvarではなく、valである
- Scalaの式はこれ以上のことができるが、この例はとっかかりとしては十分である

# Conclusion
- この章では、いくつかのScalaの基本を学んだ
- ちょっとしたScalaコードを書く機会になったのであれば幸いだ
- 次の章では、この入門的な内容が続きつつも、より先進的なトピックを学んでいく