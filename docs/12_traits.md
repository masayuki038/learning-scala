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