# 序章

- プログラムが大きくなると、管理しやすくする為に小さく分割する方法が必要になる
- 制御フローを分ける為に、Scalaはコードを関数に分割するという多くのプログラマが経験してきたものと似たアプローチを提供する
- 実際、ScalaではJavaでは表現されない関数を定義する方法がいくつかある
- あるオブジェクトのメンバ関数であるメソッドだけでなく、ネストした関数、関数リテラル、関数値もある

## 8.1 Methods

- 関数の一番身近な例は、あるオブジェクトのメンバとして定義された関数である
- そのような関数をメソッドと呼ぶ
- 以下のコードは、与えられたファイル名のファイルを読み、指定された長さを超える行を出力する
- 出力される行の先頭にはファイル名が出力される

```java
    import scala.io.Source

    object LongLines {

      def processFile(filename: String, width: Int) {
        val source = Source.fromFile(filename)
        for (line <- source.getLines)
          processLine(filename, width, line)
      }

      private def processLine(filename: String,
          width: Int, line: String) {

        if (line.length > width)
          println(filename +": "+ line.trim)
      }
    }
```

- `processFile`メソッドはパラメータとしてファイル名と長さをとる
- それはファイル名からSourceオブジェクトを作成し、for式の中で全文を取得している
- 3章のステップ12で説明したように、‘getLines`はファイルの、それぞれの改行を含む行を取得することができるイテレータを返す
- for式ではそれぞれの行で`processLine`というヘルパーメソッドを呼び出す処理を行う
- `processLine`メソッドは3つのパラメータを取る、ファイル名、幅、そして行
- 行の長さが指定された幅よりも長いかテストし、もしそうであれば、ファイル名とコロンとして行を出力する
- コマンドラインからの実行の為に、行の幅をコマンドラインの最初の引数に取り、次にファイル名を取るアプリケーションを作成する

```scala
  object FindLongLines {
    def main(args: Array[String]) {
      val width = args(0).toInt
      for (arg <- args.drop(1))
        LongLines.processFile(arg, width)
    }
  }
```

- 以下は、LongLines.scalaで45文字を超える行を見つける為にこのアプリケーションを使う

```scala
  $ scala FindLongLines 45 LongLines.scala
  LongLines.scala: def processFile(filename: String, width: Int) {
```

- これまで、これはオブジェクト指向言語で実行するのとかなり似ていた
- しかしながら、Scalaの関数のコンセプトはメソッドより一般的である
- Scalaで関数を表現する他の方法は次のセクションで説明する

# 8.2 Local functions

- 前セクションでの`processFile`メソッドの構築は関数型プログラミングスタイルの重要なデザインの責務を説明している
- プログラムは、そのぞれぞれがタスクを十分に実行できる小さい多数の関数に分割することができる
- このスタイルの利点はより難しいことを実行する為に柔軟に構成できるビルディングブロックを提供することができる
- それぞれのビルディングブロックはそれぞれを十分に理解することができる程度にシンプルでなくてはならない
- このアプローチのひとつの問題はすべてのヘルパー関数はプログラムの名前空間を汚染してしまうことである
- インタープリタではほとんど問題にならないが、一度関数が再利用可能なクラスやオブジェクトにパッケージされると、クラスの呼び出し元からヘルパー関数を隠したいと望む
- それらはしばしば個々には意味を持たないので、もしあとでクラスを別の方法で書き換えた場合、関数を削除できるように十分に柔軟性を保つことがしばしばある
- Javaでは、これに対する有効なツールはプライベートメソッドである
- プライベートメソッドのアプローチはScalaでも同じように動く
- 加えてScalaは次のようなアプローチをとる: 他の関数の内部で関数を定義することができる
- ローカル変数のように、ローカル関数はそれらのブロックからしか参照できない

```scala
  def processFile(filename: String, width: Int) {

    def processLine(filename: String,
        width: Int, line: String) {

      if (line.length > width)
        print(filename +": "+ line)
    }

    val source = Source.fromFile(filename)
    for (line <- source.getLines) {
      processLine(filename, width, line)
    }
  }
```

- この例では、privateメソッドである`processLine`を`processFile`のローカル関数にすることで、元のLongLinesをリファクタリングした
- そうする為に、private修飾子を除去し、`processFile`の内側に`processLine`の定義を移動した
- `processLine`は`processFile`の内側にあるローカル関数なので、外部からアクセスすることはできない
- `processLine`は他にも改善されている点がある
- 引数で渡された`filename`と`width`は変更されることなくヘルパー関数に渡されている
- ローカル関数は外側の関数の引数にアクセスすることができる
- 以下のように、`processLine`の外側の引数を使うことができる

```scala
    import scala.io.Source

    object LongLines {

      def processFile(filename: String, width: Int) {

        def processLine(line: String) {
          if (line.length > width)
            print(filename +": "+ line)
        }

        val source = Source.fromFile(filename)
        for (line <- source.getLines)
          processLine(line)
      }
    }
```

- 外側の関数の引数の使用はScalaが提供するネストの有用な例である
- 7.7章で説明するネストとスコープは、関数を含むすべてのScalaの構文に適用できる
- シンプルな責務だが、特にファーストクラス関数のある言語にはとてもパワフルである

# 8.3 First-class functions

- Scalaはファーストクラス関数を持っている
- 関数を定義して呼び出すだけでなく、無名の関数リテラルとして作成し、オブジェクトとして渡し回すことができる
- 関数リテラルは、実行時に生成されると関数のオブジェクトであるクラスにコンパイルされる
- それ故、関数リテラルと関数オブジェクトの区別は、関数オブジェクトが実行時にオブジェクトとして存在するのに対して、関数リテラルがソースコードに存在すること
- この区別はクラス(ソースコード)とオブジェクト(実行時)にとても似ている
- 以下の例は、数値に1を足す関数リテラルである

```scala
  (x: Int) => x + 1
```

- 関数オブジェクトはオブジェクトなので、変数に入れることもできる
- それらは関数でもあるので、一般的な括弧を付ける記法で関数を呼び出すことができる
- 両方のアクティビティの例は以下である

```
  scala> var increase = (x: Int) => x + 1
  increase: (Int) => Int = <function>

  scala> increase(10)
  res0: Int = 11
```

- この例はインクリメントなので、varは後で異なる関数オブジェクトを再割り当てすることができる
- 関数リテラルに1行以上のステートメントを入れたい場合は、カーリー括弧で囲って、1行に1ステートメントを記載して、コードブロックを形成する
- まさにメソッドのように、関数オブジェクトが実行されると、すべてのステートメントが実行される
- 関数から返ってきた値は、最終行で生成する式であれば何でも構わない

```scala
  scala> increase = (x: Int) => {
           println("We")
           println("are")
           println("here!")
           x + 1
         }
  increase: (Int) => Int = <function>

  scala> increase(10)
  We
  are
  here!
```

- ここまで関数リテラルと関数オブジェクトの要点を見てきた
- 多くのScalaライブラリはそれらを使う機会を与える
- 例えば、`foreach`メソッドはすべてのコレクションで利用することができる
- それは引数に関数を取り、それぞれの要素でその関数を起動する
- 以下は、どのようにリストのすべての要素を出力するかを示す

```scala
  scala> val someNumbers = List(-11, -10, -5, 0, 5, 10)
  someNumbers: List[Int] = List(-11, -10, -5, 0, 5, 10)

  scala> someNumbers.foreach((x: Int) => println(x))
  -11
  -10
  -5
  0
  5
  10
```

- もう一つの例としては、`filter`メソッドも持つコレクション型である
- このメソッドはユーザが提供するテストをパスする要素を選ぶ
- そのテストは関数を使って提供される
- 例えば、`(x: Int) =>  > 0` はフィルタとして使用することができる
- この関数は正の数にtrueを、負の数にfalseを返す
- 以下はフィルタの使用例である

```scala
  scala> someNumbers.filter((x: Int) => x > 0)
  res6: List[Int] = List(5, 10)
```

- `foreach`や`filter`のようなメソッドは後程説明する

# 8.4 Short forms of function literals

- Scalaは冗長な情報を省略し、関数リテラルを短く書けるいくつかの方法がある
- 1つはパラメータの方を省略できることでより短く書ける。以下に例を示す。

```scala
  scala> someNumbers.filter((x) => x > 0)
  res7: List[Int] = List(5, 10)
```

- Scalaコンパイラはxが必ずintegerであることを知っている
- なぜなら直前でintegerのリストをフィルタする為の関数を使っている
- これは、`someNumbers.filter()`の引数は`x`の型を決める為にその式の型を推論できるので、ターゲットタイピングと呼ばれる
- ターゲットタイピングの詳細はここではふれない
- 単に関数リテラルを書く際に引数の型を書かないところから始められる。もしコンパイラが混乱したら、型を足す
- 時間が経つにつれて、コンパイラが型推論できるケースとできないケースが分かってくるだろう
- 不要な記述を省略するもう一つの方法は、型推論された引数のまわりの括弧を省略できることだ
- 先の例では、xのまわりの括弧は不要だ

```scala
  scala> someNumbers.filter(x => x > 0)
  res8: List[Int] = List(5, 10)
```

# 8.5 Placeholder syntax

- 関数リテラルをより短く書くために、1つ以上のパラメータに対してプレースホルダーとして_(アンダースコア)を使うことができる
- それぞれのパラメータはその関数リテラルの中で一回だけ現れる場合のみ
- 例えば、値が0以上であるかどうかをチェックする関数を`_ > 0`ととても短く書ける

```scala
  scala> someNumbers.filter(_ > 0)
  res9: List[Int] = List(5, 10)
```

- この式のアンダースコアは埋めるべき空欄と考えることができる
- この空欄は関数が呼ばれる毎に関数の引数の値として埋められる
- `_ > 0`という関数リテラルは、より冗長に書いた`x => x > 0`と同じである
- 時々、_をパラメータのプレースホルダとして使った時に、コンパイラはパラメータの型を十分に推論できない
- 例えば、`_ + _`と書くとすると、以下のようになる

```scala
  scala> val f = _ + _
  <console>:4: error: missing parameter type for expanded
  function ((x$1, x$2) => x$1.$plus(x$2))
         val f = _ + _
```

- このようなケースでは、コロンを使って型を指定する

```scala
  scala> val f = (_: Int) + (_: Int)
  f: (Int, Int) => Int = <function>

  scala> f(5, 10)
  res11: Int = 15
```

- `_ + _`は2つのパラメータを取る関数のリテラルに展開されることを覚えておくべきである
- これはそれぞれのパラメータが関数に一度しか現れない場合のみ使える短い記法であるからだ
- 複数の_は複数のパラメータを意味し、一つのパラメータが複数回出現しているわけではない
- この最初の_は最初のパラメータを、2番目の_は2番目のパラメータを、3番目の_は3番目のパラメータを意味する

# 8.6 Partially applied functions

- 前の例は個々のパラメータの場所に_を配置したが、パラメータリスト全体を_として配置することもできる
- 例えば、`println(_)`よりはむしろ、`println _`と記載する

```scala
someNumbers.foreach(println _)
```

- Scalaはこの短い記述を次のように書いたものとみなす

```scala
someNumbers.foreach(x => println(x))
```

- このように、このケースの_は単一のパラメータのプレースホルダではなく、パラメータリスト全体のプレースホルダとなる
- このように_を使う場合、部分適用を書いている
- Scalaでは、必要な引数を渡して関数を呼び出す時、その関数に引数を適用している

```scala
scala> def sum(a: Int, b: Int, c: Int) = a + b + c
  sum: (Int,Int,Int)Int
```

- 例えば上記のような関数があって、引数に1,2,3を適用すると、以下のようになる

```scala
  scala> sum(1, 2, 3)
  res12: Int = 6
```

- 部分適用は関数によって必要とされるすべての引数が与えられていない式である
- 代わりに、必要とされる引数うのいくつかを与えるか、あるいは全く与えない
- 例えば、先程の`sum`関数に全く引数を与えない場合、以下のようになる

```scala
  scala> val a = sum _
  a: (Int, Int, Int) => Int = <function>
```

- このコードは、Scalaは`sum _`という部分適用によって、3つのパラメータが指定されていない`sum`関数を生成し、その関数の参照を`a`に割り当てる
- この新しい関数値に3つの引数を適用すると、それらが関数に渡り、`sum`関数が起動する

```scala
  scala> a(1, 2, 3)
  res13: Int = 6
```

- Scalaコンパイラは`a(1,2,3)`という式を、関数値に対するapplyメソッドの呼び出しに変換する
- それゆえ、`a(1,2,3)`は以下を短く書いたものである

```scala
  scala> a.apply(1, 2, 3)
  res14: Int = 6
```

- apply関数は、`sum _`という式からScalaコンパイラが自動的に生成したクラスに定義されているメソッドである
- それは単に3つのパラメータを`sum`に渡して結果を返す関数である
- このケースでは、applyは`sum(1,2,3)`を実行し、6という結果を返す
- _をパラメータリスト全体として使用するこの種の式のもう一つの考え方は、`def`を関数値に変換する方法である
- 例えば、`sum(a: Int, b: Int, c: Int): Int`のようなローカル関数がある場合、同じパラメータリストを取り同じ結果を返す`apply`メソッドを関数値でwrapすることができる
- この関数値に引数を適用する時、`sum`関数にそれらの引数を順番に適用し、結果を返す
- メソッドやネストした関数を割り当てたり、もう一方の関数へ引数を渡したりすることはできないが、関数名の後に_を配置することによって、メソッドやネストした関数を関数値でwrapすることができる
- `sum _`は確かに部分適用された関数であるけれども、何故これを呼び出すのか明らかではない
- `sum _`の場合は、引数を全く適用していない
- しかし、全引数ではなくいくつかの引数を部分適用することもできる

```scala
  scala> val b = sum(1, _: Int, 3)
  b: (Int) => Int = <function>
```

- このケースでは、`sum`の最初と最後の引数を提供しているが、真ん中の引数は指定していない
- ひとつの引数が抜けているで、Scalaは1つの引数を取る関数クラスを生成する
- それに一つの引数を与えて呼び出すと、この生成された関数の`apply`メソッドは1つの引数を渡してsum関数を呼び出す

```scala
  scala> b(2)
  res15: Int = 6
```

- もし`println _`や`sum _`のようにすべてのパラメータを省略した部分適用関数を書く場合、_を省略することができる

```scala
 someNumbers.foreach(println _)
```

- 上記のようなケースを以下のように書くことができる

```scala
 someNumbers.foreach(println)
```

- 最後のケースは、この例の`foreach`の呼び出しのように、関数が必要になった箇所でのみ書くことができる
- コンパイラは関数がこのケースで必要とされることを知っている、なぜなら`foreach`は一引数の関数を取るからである
- 関数を取る場所以外でこのような記述を試みるとエラーになる

```scala
  scala> val c = sum
  <console>:5: error: missing arguments for method sum...
  follow this method with `_' if you want to treat it as
     a partially applied function
         val c = sum
                 ^
  scala> val d = sum _
  d: (Int, Int, Int) => Int = <function>

  scala> d(10, 20, 30)
  res17: Int = 60
```

# 8.7 Closures

- この章のここまでは、関数リテラルのすべての例は、渡されたパラメータだけを参照してきた
- 例えば、`(x: Int) => x > 0`では、関数のボディで使用されている`x`は関数のパラメータとして定義されていた
- しかしながら、他の箇所で定義された関数を参照することもできる

```scala
(x: Int) => x + more // how much more?
```

- この関数は引数に`more`を加算する
- この関数の視点からだと、`more`はその関数リテラル自身が意味を持たせているわけではないので、自由変数である
- 対照的に、変数`x`は、その関数のコンテキストで意味を持つので、束縛変数である
- どこにも定義していないものを関数リテラルで使用すると、コンパイラはエラーを出す

```scala
 scala> (x: Int) => x + more
  <console>:5: error: not found: value more
         (x: Int) => x + more
                         ^
```

- もう一方で、同じ関数リテラルでも、`more`という名前の利用可能な何かがある限り、ちゃんと動く

```scala
  scala> var more = 1
  more: Int = 1

  scala> val addMore = (x: Int) => x + more
  addMore: (Int) => Int = <function>

  scala> addMore(10)
  res19: Int = 11
```

- この関数リテラルから実行時に作成される関数値をクロージャと呼ぶ
- 名前の由来は、自由変数の束縛をキャプチャリングする関数リテラルを包み込む振る舞いからである
- `(x: Int) => x + 1`のような、自由変数を参照しない関数リテラルはクローズドタームと呼ばれ、タームはコードの一部である
- それゆえ、この関数リテラルから実行時に作成された関数値は、厳密にはクロージャではない
- なぜなら、`(x: Int) => x + 1`はすでに閉じているからである
- しかしながら、`(x: Int) => x + 1`のような自由変数を使う関数リテラルはオープンタームである
- それゆえ、`(x: Int) => x + more`という関数リテラルから作成された関数値は`more`という自由変数を束縛する為にキャプチャする必要がある
- それゆえ、関数値がオープンタームをクロージングする振る舞いなので、`more`をキャプチャした関数値はクロージャと呼ばれる
- この例は次の質問を想起させる: クロージャを作成した後に`more`の値を変更したら何が起こるのか？Scalaでは、その答えはクロージャは変更後の値を参照する

```scala

  scala> more = 9999
  more: Int = 9999

  scala> addMore(10)
  res21: Int = 10009
```

- Scalaのクロージャは変数をキャプチャしており、変数が参照している値をキャプチャしているわｋではない
- 前の例は、クロージャの外で変更した`more`の値が`(x: Int) => x + more`のクロージャから見えている
- 反対方向も同様である
- クロージャによってキャプチャされた変数はクロージャの外でも参照できる

```scala
  scala> val someNumbers = List(-11, -10, -5, 0, 5, 10)
  someNumbers: List[Int] = List(-11, -10, -5, 0, 5, 10)

  scala> var sum = 0
  sum: Int = 0

  scala> someNumbers.foreach(sum +=  _)

  scala> sum
  res23: Int = -11
```

- この例は、リストの中の数値を加算する回り道をした方法である
- `sum`変数は、数値を足していく為に、`sum += _`という関数リテラル(`sum += _`)の中にある
- 実行時にクロージャがsumの値を書き換えるけれども、トータル-11という結果はクロージャの外でも見れる
- もしクロージャがプログラムが実行された時にいくつか異なるコピーを持つ変数にアクセスしたらどうなるのでしょう？
- 例えば、クロージャがいくつかの関数のローカル変数を使い、そしてその関数が何度も呼ばれる場合はどうなるのでしょう？
- 変数のどのインスタンスがそれぞれのアクセスで使われるのでしょうか？
- 唯一の答えは次のようになる: 使用されるインスタンスはクロージャが作成された時にアクティブだったものである
- 例えば、以下の例は`increase`クロージャを作成して返す関数である

```scala
  def makeIncreaser(more: Int) = (x: Int) => x + more
```

- この関数が呼ばれるたびに、新しいクロージャが作成される
- それぞれのクロージャはそのクロージャが作成された時にアクティブだった`more`変数にアクセスする

```scala
  scala> val inc1 = makeIncreaser(1)
  inc1: (Int) => Int = <function>

  scala> val inc9999 = makeIncreaser(9999)
  inc9999: (Int) => Int = <function>
```

- `makeIncreaser(1)`を呼び出す時、`more`に対して1を束縛した状態をキャプチャしたクロージャが作成されて返される
- 同じように、`makeIncreaser(9999)`を呼び出した時、`more`に対して9999を束縛した状態をキャプチャしたクロージャが返される
- これらのクロージャに値を適用した時、クロージャが作成された時に`more`がどのように定義されたかによって結果が返ってくる

```scala
  scala> inc1(10)
  res24: Int = 11

  scala> inc9999(10)
  res25: Int = 10009
```

- この場合の`more`がメソッド呼び出しのパラメータであることに違いはない
- Scalaコンパイラはキャプチャされたパラメータがスタックの代わりにヒープに存在するように事態を調整する
- それゆえ、パラメータはメソッド呼び出しよりも長いスコープで存続する

# 8.8 Repeated parameters

- Scalaは関数の最後のパラメータが繰り返されることを明記することができる
- これはクライアントに関数へ可変長のリストの変数を渡すことができる
- 繰り返されるパラメータを示す為、パラメータの型の後にアスタリスクをつける

```scala
  scala> def echo(args: String*) =
           for (arg <- args) println(arg)
  echo: (String*)Unit
```

- このようにすると、`echo`は0から多数の文字列を指定して呼び出すことができる

```scala
  scala> echo()

  scala> echo("one")
  one

  scala> echo("hello", "world!")
  hello
  world!
```

- 関数の内部では、繰り返されるパラメータの型は指定された型の配列になる
- それゆえ、`echo`関数の内部では引数の型は、`String*`は実際には`Array[String]`になる
- もし適切な型の配列を持っていて、それを繰り返しのパラメータとして渡すと、コンパイルエラーになる

```scala
  scala> echo(arr)
  <console>:7: error: type mismatch;
   found   : Array[java.lang.String]
   required: String
         echo(arr)
              ^
```

- これを行うには、配列の引数の後にコロンと`_*`シンボルを追加する必要がある

```scala
  scala> echo(arr: _*)
  What's
  up
  doc?
```

- この記法はコンパイラに対し、一つの引数としてではなく、配列のそれぞれの要素を`echo`に渡すことを示す

# 8.9 Tail recursion

- 7.2章において、varsを更新するwhile loopを、valsだけを使ったより関数的なスタイルに変換することについて説明した
- 時々再帰呼出しを使う必要があるだろう
- 以下の例は、十分な近似値を得る為に繰り返し計算する例である

```scala
  def approximate(guess: Double): Double =
    if (isGoodEnough(guess)) guess
    else approximate(improve(guess))
```

- このような関数は、`isGoodEnough`や`improve`の適切は実装を使って、探査系の処理で使われることが多い
- もしこの関数をより速くしたい場合、以下のようにしてwhile loopとして書いてスピードアップするかどうか試してみたくなる

```scala
  def approximateLoop(initialGuess: Double): Double = {
    var guess = initialGuess
    while (!isGoodEnough(guess))
      guess = improve(guess)
    guess
  }
```

- 2つの近似値を求める関数のどちらがより好ましいだろうか？
- 簡潔に書けてvarsを使うことを避けられているという点で、最初の関数スタイルの方が勝る
- しかし命令的アプローチは本当により効果的なのだろうか？
- 実際に実行時間を計測してみると、ほぼ同じであることが分かった
- これは驚きである、何故なら、再帰呼出しはloopの終わりで単に先頭にジャンプするのと比べるとより高コストになるように見える
- しかしながら、上記の近似値の場合、Scalaコンパイラは重要な最適化を適用することができる
- Note: その再帰呼び出しが近似値を求める関数の本体の最後に発生していることに注目してほしい
- 近似値のような関数の場合、最後のアクションで自分自身を呼び出すことを末尾再帰と呼ぶ
- Scalaコンパイラは末尾再帰を見つけ、新しい値で関数の引数を更新した後に、関数の先頭にジャンプして戻るように置き換える
- モラルは再帰呼び出しを使って問題を解く為にシャイになるべきではない
- しばしば、再帰呼出しはloopベースよりもエレガントで短く書くことができる
- そのソリューションは末尾再帰で、実行時のオーバーヘッドを気にしなくて良い

## Tracing tail-recursive functions

- 末尾再帰関数は、おのおのの呼び出しで新しいスタックフレームを作成しない
- すべての呼び出しは単一のフレームで行われる
- これはプログラマが失敗したプログラムのスタックトレースを調査する時に驚く
- 例えば、この関数は何度か自分自身を呼び出して、例外をスローする

```scala
  def boom(x: Int): Int =
    if (x == 0) throw new Exception("boom!")
    else boom(x - 1) + 1
```

- この関数は末尾再帰関数ではない、何故なら再帰呼出しの後にインクリメント操作を実行しているからだ
- それを実行すると、期待通りの結果が返る

```scala
  scala>  boom(3)
  java.lang.Exception: boom!
        at .boom(<console>:5)
        at .boom(<console>:6)
        at .boom(<console>:6)
        at .boom(<console>:6)
        at .<init>(<console>:6)
  ...
```

- `boom`を変更すると、末尾再帰になる

```scala
  def bang(x: Int): Int =
    if (x == 0) throw new Exception("bang!")
    else bang(x - 1)
```

- 実行すると、以下の結果になる

```scala
  scala> bang(5)
  java.lang.Exception: bang!
        at .bang(<console>:5)
        at .<init>(<console>:6)
  ...
```

- この時、`bang`の単一のスタックフレームだけが現れる
- スタックトレースを確認する時、もし末尾再帰で混乱した場合は、以下を設定して末尾再帰しないようにすることもできる

```
  -g:notailcalls
```

- この設定をすると、以下のように表示される

```scala
  scala> bang(5)
  java.lang.Exception: bang!
        at .bang(<console>:5)
        at .bang(<console>:5)
        at .bang(<console>:5)
        at .bang(<console>:5)
        at .bang(<console>:5)
        at .bang(<console>:5)
        at .<init>(<console>:6)
  ...
```

## Limits of tail recursion

- Scalaの末尾再帰の使用はかなり制限されている
- なぜなら、JVMのインストラクションセットがより進んだ末尾再帰の形式になるように実装することをとても難しくしている
- Scalaだけは、同じ関数の呼び出しをする再帰呼び出しを直接最適化する
- もし再帰が間接的だったら、交互に呼ばれる2つの再帰関数は最適化できない

```scala
  def isEven(x: Int): Boolean =
    if (x == 0) true else isOdd(x - 1)
  def isOdd(x: Int): Boolean =
    if (x == 0) false else isEven(x - 1)
```

- 最後の呼び出しが関数値の場合も、末尾再帰の最適化は行われない

```scala
  val funValue = nestedFun _
  def nestedFun(x: Int) {
    if (x != 0) { println(x); funValue(x - 1) }
  }
```

- `funValue`変数は`nestedFun`の呼び出しをwrapしている関数値を参照している
- 関数値に引数を適用すると、同じ引数の値が`nestedFun`に適用されて、値が返る
- それゆえ、Scalaコンパイラは末尾再帰の最適化を行うと期待するかもしれないが、このケースはそうならない
- それゆえ、末尾再帰最適化はメソッドやネストしたメソッドを最後に直接呼び出す場合に限定される

# Conclusion

- Scalaはメソッドに加えて、ローカル関数、関数リテラル、関数値を提供する
- 通常の関数呼び出しに加えて、部分適用された関数や可変長引数の関数を提供する
- 可能な時は、関数呼び出しは末尾呼び出しの最適化が行われる
- それゆえ、多くの見た目が良い再帰関数が、自分で最適化したようなwhile loopを使うのと同じくらい速く動く

# 単語

- pollute: ～を汚染する、～の神聖さを汚す
- desirable: 望ましい、好ましい、価値のある
- pass around: ～を分配する、与える、順に回す
- distinction: 区別、特徴、卓越
- nuts and bolts: 基本、要点、ポイント
- precise: 正確な、ちょうどその、ぴったりの
- substitute: ～を代わりにする、置換する
- turn around: 回転する、方向転換する、後ろを向く
- in turn: 順番に
- intuitively: 心や本能で直接感じ取って、直観的に、本能的に
- roundabout: メリーゴーラウンド、回転木馬
- roundabout way: 迂回路、回り道
- consistent with: ～と一致する、～と調和する
- denote: ～を意味する、示す、～の印である
- preferable: 一層良い、より好ましい、より望ましい
- brevity: 短さ、簡潔さ
- tempted to: ～する気にさせられる、～したくなる、～した方がいいという気になる
- mutually: 互いに、共通に、双方に