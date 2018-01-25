# 序章
- ここまでの2つの章で、クラスとオブジェクトの基本は見てきた
- この章ではもう少し詳細に見ていく
- クラスやフィールド、メソッドについて学び、セミコロンインターフェースの概要を知ることができる
- さらにScalaのアプリケーションででシングルトンオブジェクトをどのように使うかを学ぶ
- Javaに馴染みがあれば、Scalaに同じようなコンセプトを見つけるだろうが、正確には同じではない
- Javaの達人だったとしても、読むに値する

# 4.1 Classes, fields, and methods
- クラスはオブジェクトの設計図である
- 一旦クラスを定義したら、`new`キーワードを使ってクラスからオブジェクトを作成することができる

```scala
class ChecksumAccumulator {
  // class definition goes here
}
```

```scala
new ChecksumAccumulator
```

- クラスの定義は、フィールドとメソッドの2つを持つ。それらはひとまとめにメンバと呼ばれる
- フィールドは、valやvarで定義され、オブジェクトを参照する
- メソッドはdefで定義され、実行可能なコードを持つ
- フィールドはオブジェクトの状態やデータを持ち、一方でメソッドはそのデータをオブジェクトの計算処理で使用する
- クラスのインスタンスを作ると、ランタイムはオブジェクトの状態を保存する為のメモリを別途確保する
- 例えば、ChecksumAccumulatorクラスにsumというフィールドを定義したとする

```scala
class ChecksumAccumulator {
  var sum = 0
}
```

- そしてこのクラスのオブジェクトを2つ生成する

```scala
val acc = new ChecksumAccumulator
val csa = new ChecksumAccumulator
```

- sumはChecksumAccumulatorのクラスでvarフィールドとして宣言されているので、後から別の値を割り当てることがでできる

```scala
acc.sum = 3
```

- それぞれのオブジェクトが自身の値セットを持つので、フィールドはインスタンス変数として知られている
- 2つのオブジェクトのうちの片方のフィールドを変更しても、もう片方のオブジェクトのフィールドの値には影響しない
- また、accはvalなので、accに別のオブジェクトを割り当てることができない

```scala
// Won't compile, because acc is a val
acc = new ChecksumAccumulator
```

- つまり、accは一度割り当てたら常に同じChecksumAccumulatorのオブジェクトを参照するが、フィールドは時間とともに変わる
- オブジェクトの堅牢性を保つ重要な方法は、オブジェクトの状態 - インスタンス変数の値 - が常に正しい状態を保っていることである
- この最初のステップは、外部からフィールドを直接変更しないようにする為に、フィールドをprivateにすることである
- privateメソッドは同じクラスからしかアクセスできないので、状態を変更できるすべてのコードをクラスの中に閉じ込める
- privateフィールドを宣言するには、フィールド宣言の先頭にprivateをつける

```scala
class ChecksumAccumulator {
  private var sum = 0
}
```

- `ChecksumAccumulator`はこのように定義されると、クラスの外からsumにアクセスしようとしても失敗する

```scala
val acc = new ChecksumAccumulator
ass.sum = 5 // Won't compile, because sum is private
```

- Note: フィールドをpublicにするには、アクセス修飾子を指定しなければ良い
- 別の言い方をすると、Javaはpublicと指定する必要があるが、Scalaは何も指定する必要がない
- publicはScalaのデフォルトのアクセスレベルである

- 今、sumはprivateなので、そのクラスの中のコードでしかsumにアクセスすることができない
- このように、ChecksumAccmulatorはsumを操作するメソッドを定義しない限り、あまり役に立たない

```scala
class ChecksumAccumulator {
  private var sum = 0

  def add(b: Byte): Unit = {
    sum += b
  }
  def checksum(): Int = {
    return ~(sum & 0xFF) + 1
  }
}
```

- Scalaでは、メソッドの引数はvalである
- ChecksumAccumulatorに、必要なメソッドを追加したけれども、これらはもっと簡潔に書くことができる
- まず、checksumメソッドの最後のreturnは不要なので、省略できる
- 明示的にreturnステートメントがない場合、そのメソッドで最後に評価された値が返る
- 推奨するメソッドのスタイルは、reutrnの明記をしないこと、特に複数のreturnの明記は避ける、というものである
- returnを記載する代わりに、それぞれのメソッドは一つの値を返す式と考える
- この哲学はメソッドを小さくし、大きなメソッドを複数の小さなメソッドに分割しやすくする
- 一方で、設計の選択はコンテキストに依る。もし望むならば、Scalaで複数のreturnを明記するようにも書ける
- すべてのchecksumは値の計算なので、明示的なreturnは不要である
- メソッドを短くするもう一つの方法は、メソッドが一つの式だけ計算するのであれば、カーリー括弧を省略することだ
- もし結果を返す式が短いのであれば、defと同じ行に記載することもできる

```scala
class ChecksumAccumulator {
  private var sum = 0
  def add(b: Byte): Unit = sum + b
  def checksum(): Int = ~(sum & 0xFF) + 1
}
```

- addメソッドのように戻り値がUnitのメソッドは副作用がある
- このようなメソッドのもう一つの書き方は、=記号を省略して、関数本体をカーリー括弧で囲むことである

```scala
class ChecksumAccumulator {
  private var sum = 0
  def add(b: Byte) {sum + b}
  def checksum(): Int = ~(sum & 0xFF) + 1
}
```

- 関数本体の手前の=記号を省略すると、戻り値の型は基本的にUnitになる
- 以下の例は、最終結果が文字列だったとしても、=記号をつけないと、文字列をUnitに変換して値が失われる

```scala
scala> def f(): Unit = "this String gets lost"
f: ()Unit
```

- =記号のの代わりにカーリー括弧をつけても、明示的にUnitと記載した場合と同じである

```scala
scala> def g() {"this String gets lost too"}
```

- Unitではない値を返したい場合は、=記号をつければ良い

```scala
scala> def h() = {"this String gets returned!"}
h: ()java.lang.String

scala> h
res0: java.lang.String = this String gets returned
```

# 4.2 Semicolon inference
- Scalaでは行末のセミコロンは不要
- 但し、複数のステートメントを1行に書く場合は、ステートメントをセミコロンで区切る

```scala
val s = "hello"; println(s)
```

- もし、複数行に跨るステートメントを書き込む場合、ほとんどの場合は単に改行を入力すれば良いだけで、Scalaは適切にステートメントを分ける
- 例えば、次の例は4行を扱う

```scala
if (x < 2)
  println("too small")
else
  print("ok")
```

- 以下の例は、Scalaが2つに分けてしまうケースである

```scala
x
+ y
```

- このステートメントはは、`x`と`+y`の2つに分けられる
- `x+y` と認識させたい場合は、括弧で囲むことができる

```scala
(x
+ y)
```

- 一つの方法として、行末に`+`を配置する
- `+`のような操作が続くときは、行頭ではなく行末に演算子を置くのはScalaの一般的なスタイルである
- ステートを分割する正確なルールは、意外とシンプルである
- 要するに、以下のうちの一つに当てはまらない限り、行末をセミコロンとして扱う
  - 1. 問題の行が、ステートメントの終わりが演算子やピリオドなどの通常とは異なる単語の場合
  - 2. 次の行が、ステートメントを始められない単語である場合
  - 3. ()や[]の内側で終わっている場合

# 4.3 Singleton objects

```
import scala.collection.mutable.Map

object ChecksumAccumulator {
  object ChecksumAccumulator {
    private val cache = Map[String, Int]()

    def calculate(s: String): Int =
      if (cache.contains(s))
        cache(s)
      else {
        val acc = new ChecksumAccumulator
        for (c <- s)
          acc.add(c.toByte)
        val cs = ac.checksum()
        cache += (s -> cs)
        cs
      }
  }
}
```

- Scalaにはstaticメンバいないが、代わりにSingletonオブジェクトがある
- Singletonオブジェクトを定義するには、classキーワードではなくobjectキーワードを使う
- 前記のChecksumAccumulatorという名前のSingletonオブジェクトは、同じファイル内のChecksumAccmulatorクラスと同じ名前である
- クラス名と同じ名前のSingletonオブジェクトをコンパニオンオブジェクトと呼ぶ
- 同じく、クラス名をコンパニオンクラスと呼ぶ
- コンパニオンオブジェクトとコンパニオンクラスは各々のprivateメンバにアクセスできる
- SingletonのChecksumAccumulatorはStringを引数に取るcalculateという1つのメソッドを持つ
- また、以前計算したチェックサムを保存しておくcacheというmutable Mapを持つ
- ifはは、引数で指定された文字列がすでにcacheに登録されているかどうか確認し、登録されていればそれを返す
- elseは、ChecksumAccumulatorのインスタンスを作成してsのチェックサムを計算し、cacheにその結果を保存して、チェックサムを返す
- Singletonオブジェクトのメソッドは、Javaのstaticメンバと同じように、`クラス名.メソッド名`で呼び出す

```scala
ChecksumCalculator.calculate("Every value is an object.")
```

- Singletonオブジェクトはstaticメソッドを持つオブジェクト以上のもので、first-classオブジェクトである
- それゆえ、Singletonオブジェクトの名前はオブジェクトに付与された名前と考えることができる
- Singletonオブジェクトは型を定義しているわけはない
- 型はコンパニオンクラスが定義している
- しかし、Singletonオブジェクトはクラスを継承したり、Traitをミックスインすることができる
- また、継承したクラスのメソッドやミックスインしたTraitのメソッドを呼び出すことができる
- Singletonオブジェクトとクラスの一つの違いは、Singletonオブジェクトはパラメータを指定して作成することができない
- Singletonオブジェクトはnewキーワードを指定してインスタンス化することができない
- おのおののSingletonオブジェクトはstatic変数から参照されるsynthetic classというインスタンスとして実装されているので、Javaのstaticの初期化と同じ作法になる
- Singletonオブジェクトは最初にアクセスした契機で生成される
- コンパニオンクラスとは異なる名前のSingletonオブジェクトは、standalone objectと呼ばれる
- standalone objectは、ユーティリティメソッドを纏めたり、Scalaアプリケーションのエントリポイントとなる



























































