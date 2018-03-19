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

```
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