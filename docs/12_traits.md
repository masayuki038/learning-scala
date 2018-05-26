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

# 単語

- in terms of: ～に関して、～の観点から、～を単位として
- large burden: 大きな負荷

