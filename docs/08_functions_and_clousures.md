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

# 単語

- pollute: ～を汚染する、～の神聖さを汚す
- desirable: 望ましい、好ましい、価値のある
- pass around: ～を分配する、与える、順に回す
- distinction: 区別、特徴、卓越
- nuts and bolts: 基本、要点、ポイント
- precise: 正確な、ちょうどその、ぴったりの