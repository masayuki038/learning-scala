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
- もし`Ordered`トレイトを使って`Rational`に比較演算子を定義すると、次のようになる

```scala
  class Rational(n: Int, d: Int) extends Ordered[Rational] {
    // ...
    def compare(that: Rational) =
      (this.numer * that.denom) - (that.numer * this.denom)
  }
```

- まず、このバージョンの`Rational`は`Ordered`トレイトをmix-inしている
- これまで見てきたトレイトとは違い、`Ordered`トレイトは型パラメータを取っている
- 型パラメータは19章まで議論しないが、今理解しておくべきことは、`Ordered[C]`(Cは比較するクラス)をmix-inする必要があることである
- この場合、`Rational`は`Ordered[Rational]`をmix-inする
- 次に、2つのオブジェクトを比較する比較メソッドを定義する必要がある
- このメソッドは、引数で指定されたオブジェクトと自分自身を比較する
- このメソッドは、2つのオブジェクトが等価であれば0を、引数で渡されたオブジェクトよりも少なければ負の数を、大きければ正の数を返す
- この場合、`Rational`の比較メソッドは分数を共通分母と分子の引き算に変換する公式を使う
- このmix-inと比較の定義を与えると、`Rational`クラスは全ての4つの比較メソッドを持つことになる

```scala
  scala> val half = new Rational(1, 2)
  half: Rational = 1/2

  scala> val third = new Rational(1, 3)
  third: Rational = 1/3

  scala> half < third
  res5: Boolean = false

  scala> half > third
  res6: Boolean = true
```

- 何かの比較によって並べることができるクラスを実装するときは常に、`Ordered`トレイトをmix-inすることを考えるべきである
- `Ordered`トレイトは`equals`を定義していない、なぜならそうすることができないからだ
- その問題は、`equals`を実装するということは、渡されたオブジェクトの型をチェックする必要があるが、型消去により`Ordered`自身はこのチェックをできない
- それゆえ、`Ordered`トレイトを継承していても`equals`メソッドを定義する必要がある。詳細は28章で説明する
- 完全は`Ordered`トレイトは、コメントや余計なものを取り除くと以下のようになる

```scala
  trait Ordered[T] {
    def compare(that: T): Int

    def <(that: T): Boolean = (this compare that) < 0
    def >(that: T): Boolean = (this compare that) > 0
    def <=(that: T): Boolean = (this compare that) <= 0
    def >=(that: T): Boolean = (this compare that) >= 0
  }
```

- `T`や`[T]`を気にする必要はない
- `T`は型パラメータで、詳細は19章で説明する
- `Ordered`トレイトを理解する為に、それを"レシーバと同じ型を扱う"と考えるだけで良い
- `Ordered`トレイトは`compare`という抽象メソッドを定義していて、それはレシーバと同じ型のオブジェクトを比較する想定になっている
- このメソッドがあることによって、`Ordred`は<, >, <=, >= の操作を提供できる

# 12.5 Traits as stackable modifications

- トレイトの一つの主要な使い方として、薄いインターフェースを豊富なものにする
- 二つ目の使い方として、クラスにスカッカブルな変更を提供する
- トレイトは、お互いに変更をスタックすることができるので、それを用いてクラスのメソッドを変更を促す
- 例えば、整数のキューの変更をスタックすることを考える
- そのキューは2つの操作を持つ: 1つはキューに整数を登録する、もうひとつはキューから整数を取り出す
- キューはFIFOなので、`put`した順番に取り出すことができる
- キューのような実装を持つクラスに対して、これらのような変更を行うトレイトを定義することができる
  - 2倍にする: キューに`put`されているすべての整数を2倍にする
  - インクリメント: キューに`put`されているすべての整数をインクリメントする
  - キューから負の数をフィルタする
- これらの3つのトレイトは変更として表現される、なぜならそれらはキューのフル実装よりむしろキューの振る舞いを変更するからである
- その3つはスタッカブルである
- それらの3つのうち好きなものを選択し、選択した変更が適用された新しいクラスを得ることができる
- abstractな`IntQueue`クラスは以下のとおり。`IntQueue`は整数をキューに登録する為の`put`と、キューから整数を削除して返す`get`を持つ

```scala
    abstract class IntQueue {
      def get(): Int
      def put(x: Int)
    }
```

- `ArrayBuffer`を使った`IntQueue`の基本的な実装は以下のとおり

```scala
    import scala.collection.mutable.ArrayBuffer

    class BasicIntQueue extends IntQueue {
      private val buf = new ArrayBuffer[Int]
      def get() = buf.remove(0)
      def put(x: Int) { buf += x }
    }
```

- `BasicIntQueue`は配列バッファをprivateフィールドに持つ
- `get`メソッドはバッファの最後のエントリを削除するのに対して、`put`メソッドは最後に要素を追加する
- 使い方は以下のようになる

```scala
  scala> val queue = new BasicIntQueue
  queue: BasicIntQueue = BasicIntQueue@24655f

  scala> queue.put(10)

  scala> queue.put(20)

  scala> queue.get()
  res9: Int = 10

  scala> queue.get()
  res10: Int = 20
```

- トレイトを用いいてこの挙動を変えてみる
- `Doubling`トレイトは、キューにputする時に整数を2倍にする
- これには2つの面白い点がある
- まず第一に、スーパークラスにIntQueueを取ることを宣言する
- この宣言は、そのトレイトがクラスにmix-inできるだけでなく、`IntQueue`を継承できることも意味する
- それゆえ、`Doubling`を`Rational`ではなく`BasicIntQueue`にmix-inできる
- 次に、トレイトは`abstract`として宣言されたメソッドで`super`を呼び出している
- そのような呼び出しは通常のクラスではできない
- しかしながらトレイトは、そのような呼び出しは実際には成功する
- トレイトにおいて、`super`を呼び出しは動的な結合なので、`Doubling`トレイトの`super`の呼び出しは、そのトレイトが具体的な実装を持った別のトレイトやクラスにmix-inされた後に限り作用する
- この取り決めは、スタッカブルな変更を実装するトレイトにとって必要になることがある
- この目的でそうする時、そのようなメソッドに`abstract override`と付ける
- この変更の組み合わせはクラスではなくトレイトのメンバにのみ許可されている
- そして、そのトレイトはメソッドの具体的な実装を持ったクラスにmix-inされる必要があることを意味する
- このような単純なトレイトにも多くのことがある。そのトレイトの使い方は以下のとおり

```scala
  scala> class MyQueue extends BasicIntQueue with Doubling
  defined class MyQueue

  scala> val queue = new MyQueue
  queue: MyQueue = MyQueue@91f017

  scala> queue.put(10)

  scala> queue.get()
  res12: Int = 20
```

- `MyQueue`には新しいコードが無い。単にトレイトをmix-inしているだけである
- この場合、クラスを用意しなくても、直接 `BasicIntQueue with Doubling`と書くことができる

```scala
    scala> val queue = new BasicIntQueue with Doubling
    queue: BasicIntQueue with Doubling = $anon$1@5fa12d

    scala> queue.put(10)

    scala> queue.get()
    res14: Int = 20
```

- 変更をスタックする方法を確認する為に、`Incrementing`と`Filtering`という2つの変更トレイトを定義する

```scala
    trait Incrementing extends IntQueue {
      abstract override def put(x: Int) { super.put(x + 1) }
    }
    trait Filtering extends IntQueue {
      abstract override def put(x: Int) {
        if (x >= 0) super.put(x)
      }
    }
```

- これらの変更を与えることで、キューにどの変更を追加するか選択することができる
- 例えば、キューに負の値をフィルタしてすべての数値に1を加える2つの特定を追加する場合は以下のようになる

```scala
  scala> val queue = (new BasicIntQueue
             with Incrementing with Filtering)
  queue: BasicIntQueue with Incrementing with Filtering...

  scala> queue.put(-1); queue.put(0); queue.put(1)

  scala> queue.get()
  res15: Int = 1

  scala> queue.get()
  res16: Int = 2
```

- mix-inする順番が重要になる
- 詳細は次のセクションで説明するが、大まかに説明すると、より右側のトレイトが最初に効果を発揮する
- mix-inされたクラスのメソッドを呼び出すと、最も右側のトレイトのメソッドが呼び出される
- そのメソッドが`super`を呼び出すと、その左隣のトレイトのメソッドが呼び出される
- 前記の例だと、まず`Filtering`の`put`が最初に呼ばれ、負の値が除去される
- `Incrementing`の`put`が2番目に呼び出され、残った数値に1を追加する
- もし順番を逆にした場合、まず最初に1を追加し、その後負の値が捨てられる

```scala
  scala> val queue = (new BasicIntQueue
             with Filtering with Incrementing)
  queue: BasicIntQueue with Filtering with Incrementing...

  scala> queue.put(-1); queue.put(0); queue.put(1)

  scala> queue.get()
  res17: Int = 0

  scala> queue.get()
  res18: Int = 1

  scala> queue.get()
  res19: Int = 2
```

- 全体的に、このスタイルのコードは素晴らしい柔軟性をもたらす
- この3つのトレイトの組み合わせと順序を用いてmix-inすることにより、16のクラスを定義することができる
- 小さなコードで大きな柔軟性を確保できるので、スタッカブルな変更を適用できるかどうか気に留めておくべきである

# 12.6 Why not multiple inheritance?

- トレイトは、複数のクラスを継承するような方法ではあるが、多くの言語で表現される多重継承とは重要な点が異なっている
- そのうちの一つ: superの解釈は特に重要である
- 多重継承において、`super`の呼び出しから呼び出されるメソッドを、どこに呼び出しが現れたかによって決めることができる
- トレイトにおいて、メソッドの呼び出しはクラスとmix-inされたトレイトの線形によって決められる
- これが前のセクションで説明した変更のスタックができるという違いである
- 線形について見ていく前に、伝統的な多重継承を持つ言語の変更のスタックの仕方をちょっと考えて見る
- 今回は次のコードを、トレイトによるmix-inではなく多重継承として解釈する

```scala
  // Multiple inheritance thought experiment
  val q = new BasicIntQueue with Incrementing with Doubling
  q.put(42)  // which put would be called?
```

- 最初の質問は、この呼出しでどのメソッドがよばれたか
- 恐らく、ルールは最後のsuperclassが勝つことなので、このケースは`Doubling`のメソッドが呼ばれているだろう
- `Doubling`は値を2倍にして`super.put`を呼び出す、それはそうだろう
- インクリメントは行われない
- 同様に、もし最初のクラスが勝つルールであれば、その結果は2倍にはならずインクリメントになる
- それ故、どちらの順序も機能しない
- プログラマは`super`がどの`superclass`のメソッドを指すのか正確に認識するのを楽しむことができる
- 例えば、次のScalaライクなコードは、`Incrementing`と`Doubling`の両方を明示的に呼び出す

```scala
  // Multiple inheritance thought experiment
  trait MyQueue extends BasicIntQueue
      with Incrementing with Doubling {

    def put(x: Int) {
      Incrementing.super.put(x) // (Not real Scala)
      Doubling.super.put(x)
    }
  }
```

- このアプローチは新しい問題を生む
- この試みの冗長さは、大した問題ではない
- 問題は、baseクラスの`put`メソッドが、1つはインクリメント、1つは2倍で、計2回呼びだされることだ
- しかし、インクリメントされたものでも2倍にされたものでもない
- 多重継承を使う時のこの問題に対するシンプルで良い解決方法は無い
- 対照的に、Scalaのトレイトの解決方法はストレートである
- 単に`Incrementing`と`Doubling`をmix-inすれば、トレイトの`super`の特殊な効用によりすべて期待どおりに動く
- 従来の多重継承と明らかに違う何かがある
- 前に触れたように、その答えは線形化である
- newを用いてクラスを生成した時、Scalaはそのクラスと継承したクラスとトレイトを、指定された順序に沿って1つにする
- それから、それらのクラスで`super`を呼び出した時、その連鎖の次のメソッドが呼び出される
- もし最後を除くすべてのメソッドが`super`を呼び出すと、正味の結果はスタックされた振る舞いになる
- 線形化の精緻な順序は言語仕様で語られる
- それは少し複雑であるが、知る必要がある主な事項はどんな線形化であっても、クラスは常にそのスーパークラスの全ての前に線形化され、トレイトがmix-inされる、ということである
- それゆえ、`super`と書くと、そのメソッドは基本的にスーパークラスやトレイトの振る舞いを変更している
- note: このセクションの残りは線形化の詳細について述べられている
- 今、線形化の詳細に興味がなければスキップしても良い
- Scalaの線形化の主な特徴は、次の例で説明される
- `Animal`を継承し、`Furry`と`FourLegged`というトレイトをmix-inした`Cat`である
- `FourLegged`は`HasLegs`を継承している

```scala
  class Animal
  trait Furry extends Animal
  trait HasLegs extends Animal
  trait FourLegged extends HasLegs
  class Cat extends Animal with Furry with FourLegged
```

- `Cat`のクラスヒエラルキーと線形化について、図12.1に示す
- 継承はUML記法で記載されている
- 矢印の先が三角形になっている白い矢印は継承を意味し、矢印の先はスーパータイプを表す
- 矢印の先が三角形になっていない黒い矢印は線形化を意味し、矢印は`super`の呼び出しが解決される向きである
- `Cat`の線形化は後ろから前に向かって順次計算される
- `Cat`の線形化の最後にあたるのは`Animal`である
- この線形化はどんな変更もなくコピーされる
- なぜなら`Animal`は明示的にスーパークラスの継承やスーパートレイトのmix-inを行っていないので、`Any`を継承した`AnyRef`を継承したことになる
- それゆえ、`Animal`の線形化は以下のようになる

```
Animal -> AnyRef -> Any
```

- 最後から二つ目は最初にmix-inされている`Furry`トレイトだが、`Animal`の線形化において既に出てきたすべてのクラスは今省略されているので、`Cat`の線形化ではそれぞれのクラスが一度だけ出てくる

```
Furry -> Animal -> AnyRef -> Any
```

- これに先立ち、`FourLegged`の線形化が行われる
- ここでは、スーパークラスまたは最初のmi-xinの線形化ですでにコピーされたクラスはすべて省略される

```
FourLegged -> HasLegs -> Furry -> Animal -> AnyRef -> Any
```

- 最後に、`Cat`の線形化の最初のクラスは`Cat`自身である

```
Cat -> FourLegged -> HasLegs -> Furry -> Animal -> Any -> Ref
```

- これらのクラスやトレイトのいずれかが`super`を経由してメソッドを呼び出した場合、その実装は線形化のひとつ右の実装を呼び出す

# 12.7 To trait, or not to trait?

- 再利用可能な振る舞いの集合を実装した時はいつも、トレイトか抽象クラスのどちらを使いたいか決める
- 複数の箇所で再利用され、クラスに関係無いのであれば、トレイトを使う。トレイトだけがクラス階層の異なったパーツにmix-inすることができる
- Javaコードから継承したい場合は、抽象クラスを使う
  - トレイトのコードはアナログなJavaコードには合わないので、Javaクラスのトレイトからの継承はぎこちない傾向にある
  - 一方でScalaクラスからの継承は、Javaクラスからの継承とほぼ同じである
  - 一つの例外は、抽象メンバだけを持つScalaのトレイトはダイレクトにJavaのインタフェースに置き換わる
  - それゆえ、たとえJavaコードでそのようなトレイトを継承するにしても、それを定義することについて気にする必要はない
- コンパイルした形でそれを配賦し、外部のグループがそれを継承する場合、抽象クラスを使うようにする
  - その問題は、トレイトがメンバを追加/削除した場合、例え変更が無くても、それを継承したクラスはリコンパイルする必要がある
  - 外部のクライアントがその振る舞いを呼び出すだけであれば、継承するかわりにトレイトを使うのは良い
- 効率が非常に重要なのであれば、クラスを使うようにする
  - 大部分のJavaランタイムは、インタフェースのメソッド呼び出しよりもクラスメンバの仮想メソッド呼び出しを速くしている
  - それゆえ、トレイトがインターフェースにコンパイルされると、ちょっとしたパフォーマンスのオーバーヘッドがでる
  - しかしならが、問題のトレイトがパフォーマンスのボトルネックになっていて、クラスにすることでそれが解消されるエビデンスがある場合のみ、この選択をするべきである
- 上記の事項を検討した上、まだわからない場合は、トレイトとして作ることから始めて見る
  - あとでいつでも変更できるし、トレイトは一般的により多くのオプションがある

# 単語

- in terms of: ～に関して、～の観点から、～を単位として
- large burden: 大きな負荷
- rectangular: 長方形の、直角の
- suppose: 思う、仮定する、推定する
- formula: 定型句、決まり文句、公式
- cruft: 嫌なもの、粗雑なつくり、ひどい結果
- stackable: 積み重ねられる、積み重ね可能な
- arrangement: 整理すること、配置すること、取り決め
- on purpose: 故意に、わざと、わざわざ～する為に
- in question: 論議されている、問題の、 当の
- interpretation: 解釈、説明、演出