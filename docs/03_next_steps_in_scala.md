# 序章
- この章は前章に続きScalaの基礎編である
- この章ではもう少し先進的な機能を紹介する
- この章を終了した時、Scalaで有用なスクリプトを書き始めることができる知識を十分に得られる
- Scalaを感じる最も良い方法は、Scalaのコードを書き始めることだ

# Step 7. Parameterize arrays with types
- Scalaでは、newを使ってオブジェクトを生成することができる
- オブジェクトを生成する際、型とパラメータを指定する
- パラメータは生成するインスタンスの設定を意味する
- コンストラクタのカッコ内にオブジェクトを渡すことによって、インスタンスのパラメータを設定する
- 例えば、以下のScalaコードは12345を指定してBigIntegerのインスタンスを生成する

```scala
val big = new java.math.BigInteger("12345")
```

- インスタンスを生成する際、ブラケット括弧に1つ以上の型を指定する
- 例えば以下の例では、greetStringは先頭行でパラメータ'3'を指定して、配列の長さ3で要素の型がStringの配列を生成する
- 型と値をパラメータに取る時、ブラケット括弧内に型が最初に来て、その後にカッコ内に値を指定する

```scala
val greetStrings = new Array[String](3)

greetStrings(0) = "Hello"
greetStrings(1) = ", "
greetStrings(2) = "world!\n"
```

- 前記のとおり、Scalaの配列は、Javaのブラケット括弧内にインデックスを指定するのではんかう、丸括弧内に指定してアクセスする
- 配列のインデックス0は、greetStrings[0]ではなくgreetStrings(0)でアクセスする
- この3行のコードは、Scalaのvalの意義を理解するのに重要なコンセプトを示す
- valの変数に一旦値を割り当てると、再び割り当てることはできない
- しかし、それが参照する先の値は変更することができる
- このケースでは、greetStringsに別の配列を割り当てることができない
- しかしArray[String]の要素は変更することができる。つまり、配列そのものはmutableである
- 最後の2行は、greetStringsの配列の各要素を次々と出力する

```scala
for (i <- 0 to 2)
  print(greetStrings(i))
```

- Scalaのもう一つの一般的なルールを示す
- メソッドが一つの引数しか取らない場合、dotあるいは括弧を付けることなく、そのメソッドを呼び出すことができる
- この例では、実際にtoメソッドは一つのInt引数を取る
- `0 to 2`というコードは`(0).to(2)`というメソッドに変換される
- この記法はメソッド呼び出しのレシーバーを明示的に指定した時のみ有効である
- `println 10`と記載することはできないが、`Console println 10`と記載することはできる
- Scalaは伝統的な意味のオペレータは持っていないので、技術的にはオペレータオーバーロードは持っていない
- 代わりに、+,-,*,/ はメソッドの名前として使える
- これにより、Step1で示した`1+2`は、実際にはオブジェクト1の+メソッドが起動し、オブジェクト2がパラメータとして渡されている

- この例のもう一つ重要なポイントは、なぜ配列の要素に括弧でアクセスできるか、というところだ
- 配列はScalaにおいて他のクラスと同様に単純なインスタンスである
- 変数に一つ以上の値を囲む括弧を適用すると、Scalaはその変数に対してapplyという名前のメソッドを実行するコードに変換する
- なので、`greetStrings(i)`は`greetStrings.apply(i)`に変換される
- このScalaの配列の要素へのアクセスは、他のメソッド呼び出しと同様である
- この原理は配列に制限されない
- オブジェクトに、括弧で引数を適用すると、どんなものでもapplyメソッドの呼び出しに変換される
- もちろんこれはオブジェクトの型にapplyメソッドが実際に定義された場合のみである
- なので、これは特別なルールではなく、一般的なルールである
- 同じように、括弧に一つ以上の引数が指定された変数に値を設定する時、コンパイラは括弧内の引数と右辺の値を取るupdateメソッドの呼び出しに変換する

```scala
greetStrtings(0) = "Hello"
```

- は、以下に変換される

```scala
greetStrings.update(0, "Hellow")
```

- これにより、以下の文は3.1の例と文法上は同じである

```scala
val greetStrings = new Array[String](3)

greetStrings.update(0, "Hello")
greetStrings.update(1, ", ")
greetStrings.update(2, "world!\n")

for (i <- 0.to(2))
  print(greetStrings.apply(i))
```

- Scalaは配列から式までをオブジェクトで扱うことによって、すべてをシンプルな発想で扱うことができる
- Javaのprimitiveとwrapperクラスの違いや、配列とオブジェクトの違いのような、特別なケースを覚える必要がない
- そのうえ、この統一性により、重大なパフォーマンスコストを被ることはない
- Scalaコンパイラは可能な限りJavaの配列やprimitive型、ネイティブな演算を利用するコードを出力する
- このステップのここまでの例はコンパイルして動くけれども、Scalaは通常の配列の初期化についてより短い書き方を提供する

```scala
val numNames = Array("zero", "one", "two")
```

- このコードは長さ3の"zero", "one", "two"の文字列で初期化された配列を生成する
- コンパイラは暗黙的に配列をArray[String]にして、文字列を渡す
- これは実際には新しい配列を返すファクトリーメソッドを呼び出している
- このapplyメソッドは引数の数分の変数を受け取る
- このメソッドはArrayのコンパニオンオブジェクトで定義される
- Javaでいうと、Arrayクラスのapplyというstaticメソッドが呼び出されるイメージ
- このapplyの呼び出し方をより詳細に書くと以下のとおりである

```scala
val numNames2 = Array.apply("zero", "one", "two")
```

# Step 8. Use lists
- 関数型スタイルの大きなアイデアの一つは、メソッドが副作用を持つべきではない、ということだ
- メソッドは計算して値を返すだけである
- このアプローチの良い効果は、メソッドがゴチャゴチャにならないことだ
- それゆえ、信頼しやすくなるし、再利用しやすくなる
- もうひとつの効果は、型チェッカーによってメソッドの入出力のすべてがチェックされることだ
- それゆえ、型エラーが現れやすくなる
- 関数型の哲学のオブジェクトへの適用は、オブジェクトをimmutableにする
- 前回説明したように、Scalaでは配列はmutableである
- 連続する同じ型のオブジェクトをimmutableで扱う為に、ScalaのListクラスが使える
- 配列と同様に、List[String]は文字列だけを含む
- ScalaのListであるscala.Listは、Javaのjava.util.Listとは違う。ScalaのListは常にimmutableである
- より一般的にいうと、ScalaのListは関数型のスタイルでプログラミングできるようなデザインになっている

```scala
val oneTwoThree = List(1, 2, 3)
```

- このコードは、Listを作成する際に1,2,3の整数値で初期化し、oneTwoThreeという名前の変数に設定する
- Listはimmutableなので、JavaのStringのような動きになる
- listのメソッドを呼び出した時、そのlistの中の値が変わるように見えるかもしれないが、実際には新しいlistが返ってくる
- Listは`:::`という名前の連結メソッドを持っている

```scala
val oneTwo = List(1, 2)
val threeFour = List(3, 4)
val oneTwoThreeFour = oneTwo ::: threeFour
println("" + oneTwo + " and " + threeFour + "were not mutated.")
println("Thus, " + oneTwoThreeFour + " is a new list.")
```

- このスクリプトを実行すると、以下のように出力される

```scala
List(1, 2) and List(3, 4) were not mutated.
Thus, List(1, 2, 3, 4) is a new list.
```

- Listの最も良く使われる演算子は"cons"と呼ばれる`::`だろう
- Consは既存のlistの先頭に新しい要素を追加し、そのlistを返す
- 例えば、以下のスクリプトを実行する

```scala
val twoThree = List(2, 3)
val oneTwoThree = 1 :: twoThree
println(oneTwoThree)
```

- これは以下のように出力される

```scala
List(1, 2, 3)
```

- 注意
  - 演算子のメソッドは通常、例えば`a * b`であれば、`a.*(b)`のように左辺のメソッドが呼び出される
  - しかし演算子の最後に`:`が付く場合、例えば`1 :: twoThree`であれば、`twoThree.::(1)`のように左辺のメソッドが呼び出される

- 空のリストを指定する簡単な方法はNilで、新しいListを作成する一つの方法は、Nilを最後の要素にしてcons演算子で結合することである

```scala
scala> val oneTwoThree = 1 :: 2 :: 3 :: Nil
oneTwoThree: List[Int] = List(1, 2, 3)
```

- Nilは新しいリストを作る為に必要で、Nilを除くとエラーになる

```scala
scala> val oneTwoThree2 = 1 :: 2 :: 3
<console>:10: error: value :: is not a member of Int
       val oneTwoThree2 = 1 :: 2 :: 3
```

- なぜListの後ろに追加しないのか
  - Listには(後ろに追加する)append操作がない
  - これは、appendはListのサイズが大きくなってくると時間が線形に増えていくのに対し、`::`(先頭に追加)は一定の時間しかかからない為だ
    - ScalaのListがhead, tailの2つの要素で構成されているので、`list.prepend(N)`は`new List(head=N;tail=list)`を返せば良い
      - immutableだが、リストの要素を全てshallow copyしたりする必要がない
        - https://stackoverflow.com/a/30648447/1352781
        - http://www.ne.jp/asahi/hishidama/home/tech/scala/collection/list.html
  - 一つのやり方としては、Listの先頭に値を追加していき、最後にreverseメソッドを呼び出す、という方法である
  - あるいはListBufferというmutableなリストを使う

# Step 9. Use tuples
- tupleはリストとは違い異なる型を格納することができる
- JavaであればJavaBeanのようなクラスを作って複数の値を格納して返すところを、Scalaではtupleで簡単に返すことができる
- tupleの生成は括弧の中に要素を,(コンマ)で区切って指定する
- tupleへのアクセスは、.(ドット)、_(アンダースコア)、そして1から始まるインデックスを使う

```scala
val pair = (99, "Luftballons")
println(pair._1)
println(pair._2)
```

- 上記の例では、Int型の99と、文字列型の"Luftballons"を指定してtupleを作っている
- Scalaはこのtupleを暗黙裡にTuple2[Int, String]に変換する
- 次に、このtupleの_1にアクセスすると、最初の要素(99)が返る
- この.(ドット)の使い方は、通常のフィールドへのアクセスややメソッド呼び出しと同じで、この場合は_1というフィールドにアクセスしている
- このスクリプトを実行すると、以下のように表示される

```sh
99
LuftBallons
```

- ('u', 'r', "the", 1, 4, "me")の型はTuple6[Char, Char, String, Int, Int, String]である
- tupleの要素に(N)でアクセスしないのは、tupleの各要素の型が同じではないから
- indexが0からではなく1から始まっているのは、静的型のHaskellやMLがそうである為

# Step 10. Use sets and maps
- Scalaは関数型、命令型の2つのスタイルの利点を使えるようにする為、コレクションライブラリはmutableとimmutableで差別化のポイントがある
- SetやMapはクラス階層によってmutableやimmutableが異なっている
- Scalaには以下の3つのSetがあり、それぞれパッケージが違う
  - scala.collection.Set
  - scala.collection.immutable.Set
  - scala.collection.mutable.Set

```scala
var jetSet = Set("Boeing", "Airbus")
jetSet += "Lear"
println(jetSet.contains("Cessna"))
```

- 上記コードのSetはデフォルトのimmutableなSetが返る
- ScalaコンパイラはjetSetの型をimmutableなSet[String]と推測する
- immutableなSetもmutableなSetも`+`メソッドを持っているが、それぞれで動きは異なる
- mutableの方は自身に要素を追加するのに対して、immutableの方は新しいSetを返す
- 上記コードはimmutableなSetなので、`+`メソッドにより新しいSetが返る
- mutableなSetは`+=`メソッドを持っているが、immutableなSetは持っていない
- このケースでは、`jetSet += "Lear"`は本質的には以下のようになる

```scala
jetSet = jetSet + "Lear"
```
- それゆえ、前記のコードの2行目は"Boeing", "Airbus", "Lear"を含む新しいSetをjetSetに再び設定している
- そして、最後の行で"Cessna"が含まれるか確認している(これは期待通り、falseが出力される)
- mutableなSetの場合はimportを使う必要がある

```scala
import scala.collection.mutable.Set

val movieSet = Set("Hitch", "Poltergeist")
movieSet += "Sherk"
println(movieSet)
```

- 最初の行でmutableなSetをimportしている
- Javaと同様に、importステートメントはパッケージ名が付いた長い名前の代わりに、Setのような短い簡潔な名前を使えるようになる
- その結果、Scalaコンパイラは3行名のSetをscala.collection.mutable.Setと認識する
- `+=`メソッドは、scala.collection.mutable.Setの`+=`メソッドである
  - immutableなSetの時のように、`movieSet = movieSet + "Sherk"`ではない

- immutableなSetやmutableなdefaultのSet実装を提供するSetFactoryメソッドは、様々なシチュエーションでどのSetが欲しいか明示するのに重要になってくる
- MapもSetと同様、immutableとmutableを持つクラス階層になっている

```scala
import scala.collection.mutable.Map

val treasureMap = Map[Int, String]()
treasureMap += (1 -> "Go to island.")
treasureMap += (2 -> "Find big X on ground.")
treasureMap += (3 -> "Dig.")
println(treasureMpa(2))
```

- 前記のコードで、Scalaコンパイラは`"1 -> "Go to island."`を`(1).->("Go to island.")`と変換する
- 故に、`1 -> "Go to island."`は、実際には値1のintegerの`->`メソッドに"Go to island"を渡している
- この->メソッドはkeyとvalueの2つの値を保存したtupleを返す
- そしてこのtupleをtreasureMapの`+=`メソッドに渡す
- defaultはimmutable Mapなので、immutable Mapを使う時、明示的なimport文は不要である

```scala
val romanNumeral = Map(
  1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V"
)
println(romanNumeral(4))
```

- この例にはimport文が無いので、1行目のMapにはdefaultのscala.collection.immutable.Mapが返ってくる
- 5つのkey/valueのtupleをmapのFactoryメソッドに渡し、渡されたkey/valueを格納するimmutable Mapを取得している

# Step 11. Learn to recognize the functional style
- varがいたるところにあったら、そのプログラムは命令型プログラミングだろう
- 関数型に移行する一つの方法は、プログラムからvarを無くすことである
- JavaやC++、C#の変数は通常、varの挙動になる
- HaskellやOCaml、Erlangの変数は、valの挙動になる
- Scalaにおいては、varもvalもどちらも有用で、悪いものではない。使い分けて良い
- この哲学に同意したとしても、コードにvarがあれば、まずvarを取り除く方法を見つけたいと思うかもしれない

```scala
def printArgs(args: Array[String]): Unit = {
  var i = 0
  while (i < args.length) {
    println(args(i))
    i += 1
  }
}
```

- 上記コードを、varを取り除いて関数型スタイルにするには、以下のようにする

```scala
def printArgs(args: Array[String]): Unit = {
  for (arg <- args) {
    println(arg)
  }
}
```

- あるいは、このようにする

```scala
def printArgs(args: Array[String]): Unit = {
  args.foreach(println)
}
```

- このプログラムはvarを少なくするメリットを示している
- リファクタリングされたコードは、元のコードと比べて、明快でより短く、エラーが少なくなる傾向になっている
- 実際、Scalaが関数型スタイルを推奨しているのは、コードをより分かりやすく、エラーが少なくなるように書けるからである















