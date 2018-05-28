# 序章

- トレイトは、Scalaによいて再利用可能な単位である
- トレイトはフィールドやメソッドの定義し、それらをクラスにmix-inすることで再利用可能とする
- クラスの継承とは異なり、それぞれのクラスは一つのスーパークラスしか継承できないが、トレイトは複数mixできる

# 12.1 How traits work

- トレイトの定義は、`trait`というキーワードを使うこと以外はクラスの定義とよく似ている

```scala
    trait Philosophical {
      def philosophize() {
        println("I consume memory, therefore I am!")
      }
    }
```

- 一旦トレイトを定義すると、クラスは`extends`か`with`といったキーワードを使ってクラスにmix-inできる
- Scalaプログラマは、トレイトをmix-inすることが、他の多くの言語にあるような多重継承と異なる重要なポイントがあるので、継承よりも"mix in"を使う
- FrogクラスにPhilosophicalトレイトをmix-inすると以下のようになる

```scala
    class Frog extends Philosophical {
      override def toString = "green"
    }
```

- `Frog`クラスは`AnyRef`のサブクラスで、Philosophicalトレイトをmix-inしている
- トレイトから継承したメソッドは、スーパークラスから継承したメソッドと同じように使える

```scala
  scala> val frog = new Frog
  frog: Frog = green

  scala> frog.philosophize()
  I consume memory, therefore I am!
```

- トレイトは型も定義する
- 型にPhilosophicalを使った時の例は以下である

```scala
  scala> val phil: Philosophical = frog
  phil: Philosophical = green

  scala> phil.philosophize()
  I consume memory, therefore I am!
```

- `phil`の型はトレイトのPhilosophicalである
- それゆえ、`phil`変数は`Philosophical`をmix-inしたどんなオブジェクトでも割り当てることができる
- 明示的にスーパークラスを継承しているクラスにトレイトをmix-inしたい場合や、複数のトレイトをmix-inしたい場合は`with`句を使う

```scala
    class Animal

    class Frog extends Animal with Philosophical {
      override def toString = "green"
    }
```

```scala
    class Animal
    trait HasLegs

    class Frog extends Animal with Philosophical with HasLegs {
      override def toString = "green"
    }
```

# 12.2 Thin versus rich interfaces

- トレイトの主要な使い方の一つは、クラスがすでに持っているメソッドに対してメソッドを自動的に追加することである
- すなわち、トレイトが薄いインターフェースを豊かにし、豊かなインターフェースにすることができる
- 薄いインターフェースと豊かなインタフェースの比較は、通常、オブジェクト指向設計におけるトレードオフとして表現される
- 実装者とインターフェースのクライアントの間にトレードオフが存在する
- 豊富なインターフェースは、多数のメソッドがあり、呼び出し側にとっては便利である
- クライアントは必要な機能に正確にマッチするメソッドを選ぶことができる
- 薄いインターフェースは一方で、より少ないメソッドしか持っておらず、実装者にとって簡単である
- しかしながら、クライアントはより多くのコードを書く必要がある
- 呼び出すことができるメソッドの選択を少なくする、ということは、必要性に完全に一致するものは選べず、それを使うために追加でコードを書く必要があるだろう
- Javaはほとんどの場合、薄いインターフェースが用いられる
- `CharSequence`を例に取ると、このクラスはScalaで定義すると以下のようになる

```scala
  trait CharSequence {
    def charAt(index: Int): Char
    def length: Int
    def subSequence(start: Int, end: Int): CharSequence
    def toString(): String
  }
```

- `String`クラスのメソッドの大部分は`CharSequence`に適用できるが、`CharSequence`は4つのメソッドしか定義していない
- `CharSequence`にすべての`String`のインターフェースを含んでいれば、`CharSequence`の実装者に大きな負担をかけることになる
- Scalaのトレイトには実装を含めることができるので、それぞれのクラスで再実装する必要はない
- それゆえ、トレイトのない言語と比べると、Scalaでは少ない作業量で豊富なインターフェースを提供できる
- トレイトを使ってインターフェースを豊かにしていくには、少数の抽象メソッド(薄い部分)と、多くの具象メソッドを定義する

# 12.3 Example: Rectangular objects

- グラフィックライブラリはしばしばいくつかの長方形を表現するためのクラスを多数持つ
- 例えば、ウィンドウやビットマップイメージ、マウスで選択された範囲等
- これらの使いやすい長方形のオブジェクトを作る為に、ライブラリがwidth, height left, right, topLeft等のような平面上の特性を問い合わせることができると良い
- しかしながら、Javaのライブラリですべての長方形のオブジェクトに対してそれらの全てを提供するライブラリを書くのは負荷が高い
- 反対に、Scalaでそのようなライブラリを書くのであれば、すべてのクラスにこれらのすべての便利なメソッドを簡単に提供できる
- トレイトを使わない場合の最初のコードのイメージは以下のようになる

```scala
  class Point(val x: Int, val y: Int)

  class Rectangle(val topLeft: Point, val bottomRight: Point) {
    def left = topLeft.x
    def right = bottomRight.x
    def width = right - left
    // and many more geometric methods...
  }
```

- Rectangleクラスはプライマリコンストラクタtop-leftとbottom-rightという2つのPointを取る
- そしてシンプルにこの2つのPointを計算することによって、`left`や`right`といった多数の便利なメソッドを実装する
- もう一つのグラフィックライブラリが持つクラスは2-Dウィジェットである

```scala
  abstract class Component {
    def topLeft: Point
    def bottomRight: Point

    def left = topLeft.x
    def right = bottomRight.x
    def width = right - left
    // and many more geometric methods...
  }
```

- この2つのクラスの`left`や`right`の定義は完全に同じである
- 長方形のオブジェクトの他のクラスにおいても、ちょっとしたバリエーションの違いはあれど、同じものになる
- この重複は豊富なトレイトによって削ることができる
- そのトレイトは2つの抽象メソッドを持っている: 1つはオブジェクトの`top-left`を返すもので、もう一つは`bottom-right`を返すものである
- それから、すべての他の平面のクエリの実装を提供することができる

```scala
    trait Rectangular {
      def topLeft: Point
      def bottomRight: Point

      def left = topLeft.x
      def right = bottomRight.x
      def width = right - left
      // and many more geometric methods...
    }
```

- `Component`クラスは`Rectangular`が提供するすべての平面に関するメソッドを得るためにこのトレイトをmix-inする

```scala
  abstract class Component extends Rectangular {
    // other methods...
  }
```

- 同様に、`Rectangle`もそのトレイトをmix-inできる

```scala
  class Rectangle(val topLeft: Point, val bottomRight: Point)
      extends Rectangular {

    // other methods...
  }
```

- これらの定義を与えると、`Rectangle`を作ることができ、`width`や`left`といった平面に関するメソッドを呼ぶことができる

```scala
  scala> val rect = new Rectangle(new Point(1, 1),
             new Point(10, 10))
  rect: Rectangle = Rectangle@3536fd

  scala> rect.left
  res2: Int = 1

  scala> rect.right
  res3: Int = 10

  scala> rect.width
  res4: Int = 9
```
# 12.4 The Ordered trait

- 比較は豊富なインターフェースが役立つもう一つの問題領域である
- 順序付けられた2つのオブジェクトを比較するときはいつも、正確な比較ができる単体のメソッドを使うと便利である
- `Ordered`トレイトを見る前に、それを使わないで比較する方法を想像してみる
- 6章の`Rational`クラスを思い出し、比較演算子を加えてみると、以下のようになる

```scala
  class Rational(n: Int, d: Int) {
    // ...
    def < (that: Rational) =
      this.numer * that.denom > that.numer * this.denom
    def > (that: Rational) = that < this
    def <= (that: Rational) = (this < that) || (this == that)
    def >= (that: Rational) = (this > that) || (this == that)
  }
```

- このクラスは4つの比較演算子(<, >, <=, >=)を定義していて、それは豊富なインターフェースを定義する古典的なコストを示す
- まず、比較演算子の3つは、1つ目の演算子で定義されていることに気づくだろう
- `>`は`<`の反対であり、`<=`は文字通り「以下」と定義される
- 加えて、これらの3つすべてのメソッドは、比較可能な他のクラスと同じである
- 有理数に対して`<=`が特別な意味を持つことはない
- 比較のコンテキストにおいて、`<=`は常に「以下」を意味する
- 結局、他のクラスに比較演算子を実装する場合と同様に、このクラスには多数のボイラープレートコードがある
- この問題はよくあるので、Scalaはそれを補助するトレイトを提供する
- そのトレイトは`Ordered`である
- それを使うと、それぞれの比較演算子を一つの比較メソッドで置き換えることができる
- `Ordered`トレイトはこの一つのメソッドで、<, >, <=, >= の4つを定義する
- それゆえ、`Ordered`トレイトは`compare`というメソッドを定義だけで、豊富な比較メソッドを得ることができる

# 単語

- in terms of: ～に関して、～の観点から、～を単位として
- large burden: 大きな負荷
- rectangular: 長方形の、直角の
- suppose: 思う、仮定する、推定する
