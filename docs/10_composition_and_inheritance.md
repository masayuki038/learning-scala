# 序章

- 6章ではScalaのオブジェクト指向の側面を紹介した
- この章では、6章では省略したものをピックアップし、Scalaのオブジェクト指向プログラミングをサポートするより優れた事項に踏み込んでいく
- クラス間の基本的な関係、集約と継承を比較する
- 集約は一つのクラスがもう一方の参照を保持しており、その参照されたクラスはそのミッションを満たすのをサポートする
- 継承は親クラス、子クラスの関係である
- これらのトピックに加えて、抽象クラス、パラメータ無しのメソッド、クラスの拡張、メソッドやフィールドのオーバーラード、パラメータ化されたフィールド、親クラスのコンストラクタの起動、ポリモーフィズム、動的なバインディング、finalのメンバやクラス、ファクトリーオブジェクトやメソッドについて議論する

# 10.1 A two-dimensional layout library

- この章の動くプログラム例として、2つの次元のレイアウト要素を構築してレンダリングするライブラリを作成する
- それぞれの要素はテキストで埋められた矩形を表現する
- 利便性から、そのライブラリは指定されたデータから新しい要素を作成する`elem`というファクトリーメソッドを提供する
- 例えば、次のようなシグネチャでファクトリーメソッドを使って、文字列を含む要素を作成することができる

```scala
 elem(s: String): Element
```

- 要素はElementという名前の型でモデル化されている
- 2つの要素を組み合わせて新しい要素を作る為に、ひとつの要素に2つめの要素を渡し、`above`や`beside`を呼び出すことができる
- 例えば次の式は、それぞれの高さが2である、2つの列で構成される大きな要素を構築する

```scala
  val column1 = elem("hello") above elem("***")
  val column2 = elem("***") above elem("world")
  column1 beside column2
```

- この式の結果を出力は以下のようになる

```
hello ***
*** world
```

- レイアウト要素は、操作を組み合わせる目的を持ったシンプルなパーツからオブジェクトを構築するシステムの良い例である
- この章では、シンプルなパーツである配列、行、矩形から構築される要素のオブジェクトのクラスを定義する
- また、`above`や`beside`という構築操作を定義する
- そのような構築操作は、いくつかのドメインの要素を組み合わせて1つの新しい要素にするので、通常コンビネータとも呼ばれる
- コンビネータについて考えることは、ライブラリを設計する上で一般的に良い方法である
- アプリケーションドメインのオブジェクトを構築するための基本的な方法について考える
- シンプルなオブジェクトとは何か？
- よりシンプルなオブジェクトから機能的なオブジェクトを構築する方法とは？
- どのようにコンビネータを組み合わせるか？
- もっとも一般的なコンビネータは何か？
- どうルールを満たすか？
- これらの問いに答えられるのであれば、ライブラリの設計は順調に進んでいる

# 10.2 Abstract classes

- 最初のタスクはレイアウト要素を表現する`Element`型を定義することである
- 要素は文字列で構成される2つの次元の矩形なので、レイアウト要素を参照する`contents`というメンバを持つ
- `contents`は、おのおのの文字列が1行になっている`Array[String]`で表現される
- それゆえ、`contents`によって返された結果の方もまた`Array[String]`である

```scala
    abstract class Element {
      def contents: Array[String]
    }
```

- このクラスでは、`contents`は実装を持たないメソッドとして宣言される
- 言い換えると、そのメソッドは`Element`クラスの抽象メンバである
- 抽象メンバを持つクラスは`class`キーワードの前に`abstract`と書くことで抽象クラスとして宣言する必要がある

```scala
  abstract class Element ...
```

- `abstract`修飾子は、実装されていない抽象メンバがあることを示す
- 結果として、抽象クラスをインスタンス化することはできない
- インスタンス化しようとすると、以下のようにコンパイルエラーがでる

```scala
  scala> new Element
  <console>:5: error: class Element is abstract;
      cannot be instantiated
         new Element
             ^
```

- この章の後で、未定義の部分を実装してインスタンス化できるように`Element`クラスのサブクラスの作り方を紹介する
- Note: `Element`クラスの実装されたメンバには`abstract`修飾子を付けない
- 実装が無いメソッドが`abstract`である
- Javaと異なり、メソッド宣言に`abstract`修飾子は必要無い
- 実装されているメソッドは`concrete`と呼ばれる
- 「宣言」と「定義」は少し異なる
- `Element`クラスは`abstract`メソッドを宣言しているが、`concrete`メソッドは定義していない
- しかしながら、次のセクションでいくつかの`concrete`メソッドを定義することで`Element`を拡張する

# 10.3 Defining parameterless methods

- 次のステップとして、幅と高さを公開するメソッドを`Element`に追加する
- `height`メソッドは行数を返す
- `width`メソッドは最初の行の長さを返すか、行が無い場合は0を返す

```scala
  abstract class Element {
    def contents: Array[String]
    def height: Int = contents.length
    def width: Int = if (height == 0) 0 else contents(0).length
  }
```

- `Element`の3つのメソッドは、空の引数リストも含めて、引数リストを持たない

```scala
 def width(): Int
```

- 上記のような記述の代わりに以下のように書く

```scala
def width: Int
```

- Scalaでは、パラメータ無しのメソッドは良く使われる
- 反対に、メソッドは`def height(): Int`のように空のパラメータリスト`()`を用いて定義される
- 推奨される慣習としては、引数が無く、オブジェクトのフィールドの値を返すだけの(特にオブジェクトの状態を変更しない)メソッドの時にパラメータ無しのメソッドを使う
- この慣習は、クライアントコードが属性やメソッドの実装内容に影響されるべきではない、という`uniform access principal`をサポートする
- この例は、`width`と`height`を単にdefをvalに変更することで、メソッドの代わりにフィールドとすることを選択する

```scala
  abstract class Element {
    def contents: Array[String]
    val height = contents.length
    val width =
      if (height == 0) 0 else contents(0).length
  }
```

- 2つの定義のそれぞれは、クライアントの視点で見ると全く等価である
- 唯一の違いは、メソッドの起動よりもフィールドを参照する方が少し速い点である
- なぜなら、フィールドの値はクラスが初期化された時に事前に計算されており、メソッドは呼び出された時に計算するからである
- もう一方で、フィールドはそれぞれの`Element`オブジェクトで別途メモリが必要である
- それゆえ、属性をフィールドかメソッドのどちらで表現するかは使われ方に依るし、使われ方は時間と共に変わるだろう
- ポイントは内部の実装が変わっても`Element`クラスのクライアントは影響を受けないようにするべきである
- 特に、`Element`クラスのクライアントは、アクセス関数が副作用がなく状態を変更しない限り、フィールドをアクセス関数に書き換える場合、書き換える必要がないようにするべきである
- クライアントはどちらの方式かを気にする必要がない
- ここまでは順調だが、Javaでこのことについて対応しようとすると少し複雑である
- 問題は、Javaが`uniform access principal`を実装していないことにある
- それゆえ、Javaでは`string.length()`であり、`string.length`とはできない
- 言うまでもなく、これはとても混乱する
- そのギャップを埋める為、Scalaは引数無しと空括弧が混在した時でも柔軟に対応できるようになっている
- 特に、引数無しのメソッドを空括弧でオーバーライドできるし、逆もできる
- また、引数を一つも取らないメソッドの呼び出しは空括弧を省略することもできる
- 例えば、Scalaでは次の2行はどちらも正しい

```scala
  Array(1, 2, 3).toString
  "abc".length
```

- 原則として、Scalaの関数呼び出しではから確固を省略することが可能である
- しかしながら、呼び出すメソッドが、レシーバオブジェクトのプロパティ以上のことを表す場合は空括弧をつけることを勧める
- 例えば、I/O処理をしたり、varsに値を設定したり、状態を持つオブジェクトを使って直接的に、あるいは間接的にレシーバのフィールド以外のvarsを読む時は、空括弧を付ける方が適している
- 対象の処理を呼び出した時の引数のリストは、見た目の手がかりとして機能する

```scala
  "hello".length  // no () because no side-effect
  println()       // better to not drop the ()
```

- まとめると、Scalaでは、副作用がなくて引数を取らないメソッドを定義する時は空括弧を省略する等してパラメータレスにすることを推奨している
- もう一方で、副作用がある場合はパラメータレスにするべきではない
- 副作用があるような呼び出しをする時は、空括弧を付けることに気をつける
- もう一つの考え方として、操作をする関数を呼び出す時には括弧を付け、単にプロパティにアクセスする時には括弧を付けない

# 10.4 Extending classes

- すでに見てきたように、`Element`がabstractなのでnewできない
- それゆえ、elementを生成する為に、`Element`のサブクラスを作って抽象メソッドである`contents`を実装する必要がある

```scala
    class ArrayElement(conts: Array[String]) extends Element {
      def contents: Array[String] = conts
    }
```

- `ArrayElement`クラスは`Element`クラスを継承している
- 継承する為には、Javaと同じようにクラス名の後に`extends`節を付ける

```scala
 ... extends Element ...
```

- 継承には2つの効果がある
- 1つめは、`ArrayElement`は`Element`のprivate以外のメンバを継承する
- 2つめは、`ArrayElement`が`Element`のサブタイプになる
- `ArrayElement extends Element`とすると、`ArrayElementクラス`は`Element`クラスのサブクラスと呼ばれる
- 反対に、`Element`は`ArrayElement`のスーパークラスである
- `extends`節を省略すると、Scalaコンパイラは暗黙的に`scala.AnyRef`を継承したこととする
- それゆえ、`Element`クラスは`AnyRef`クラスを暗黙的に継承していることになる
- 継承はスーパークラスのメンバがサブクラスのメンバでもある、ということを意味する。但し2つ例外がある
- 1つめは、スーパークラスのprivateメンバはサブクラスに継承されない
- 2つめは、サブクラス側で同じ名前と同じ引数で実装されている場合、スーパークラスのメンバは継承されない
- そのようなケースは、サブクラスのメンバがスーパークラスのメンバをオーバーライドしている、と言う
- サブクラスのそのメンバが具象メソッドで、スーパークラスのそのメンバが抽象メソッドである場合、具象メソッドは抽象メソッドを実装している、と言う
- 例えば、contentsメソッドは`Element`の抽象メソッドcontentsをオーバーライドしている
- 反対に、`ArrayElement`はwidthメソッド、heightメソッドを`Element`から継承している
- 例えば、`ArrayElement`である`ae`に対して、`ArrayElement`クラスにwidthメソッドが定義されていたかのように、`ae.width`で幅を問い合わせることができる

```scala
  scala> val ae = new ArrayElement(Array("hello", "world"))
  ae: ArrayElement = ArrayElement@d94e60

  scala> ae.width
  res1: Int = 5
```

- サブタイピングは、スーパークラスの値が必要なところで、そのサブクラスの値を使うことができることを意味する

```scala
val e: Element = new ArrayElement(Array("hello"))
```

- 変数`e`は`Element`型として定義されているので、設定する値もまた`Element`であるべきである
- 実際には、`ArrayElement`の方の値が設定されている
- `ArrayElement`クラスは`Element`クラスを継承していて、結果として`ArrayElement`の型は`Element`の型と互換性があるので、これはOKである
- 先の例では、`ArrayElement`と`Array[String]`の間に集約関係があることをも示している
- この関係は`ArrayElement`が`Array[String]`から構成されており、Scalaコンパイラは`ArrayElement`クラスを引数で渡された`conts`の参照を保持するフィールドを持ったバイナリクラスとして出力するので、集約と呼ばれる
- この章の10.11のセクションで、集約と継承の考え方について議論をする

# 10.5 Overriding methods and fields

- `uniform access principal`はScalaがJavaよりもフィールドやメソッドを区別せずに扱えるという一つの側面に過ぎない
- もう一つの違いは、フィールドとメソッドが同じ名前空間に属している点である
- これは引数無しのメソッドをフィールドでオーバーライドできるようにしている
- 例えば、`ArrayElement`のcontentsの実装を、`Element`クラスの抽象メソッドの定義を変更すること無く、メソッドからフィールドに変更することができた

```scala
    class ArrayElement(conts: Array[String]) extends Element {
      val contents: Array[String] = conts
    }
```

- このバージョンの`ArrayElement`のcontentsフィールドは`Element`クラスの引数無しのcontentsメソッドの完璧な実装である
- 一方で、Scalaでは同じクラス内の同じ名前でフィールドとメソッドを定義することが許されていないが、Javaは許されている
- 例えば、以下のJavaコードはコンパイルできる

```java
  // This is Java
  class CompilesFine {
    private int f = 0;
    public int f() {
      return 1;
    }
  }
```

- しかしこれに対応するScalaのクラスはコンパイルできない

```scala
  class WontCompile {
    private var f = 0 // Won't compile, because a field
    def f = 1         // and method have the same name
  }
```

- 一般的に、Javaに4つの名前空間があるのに対し、Scalaは2つしかない
- Javaの4つの名前空間は、フィールド、メソッド、型、そしてパッケージである
- 対してScalaは、`values`(フィールド、メソッド、パッケージ、シングルトンオブジェクト)と`types`(クラスとトレイト)のみである
- Scalaがフィールドとメソッドを同じ名前空間に配置している理由はまさしく、引数無しのメソッドを`val`でオーバーライドできる点にある

# 10.6 Defining parametric fields

- 前のセクションの`ArrayElement`のクラスの定義について再度考えて見ると、単に`contents`フィールドにコピーするという目的で`conts`というパラメータを取っている
- `conts`の名前は、`contents`というフィールド名っぽいもので衝突しないように付けられたように見える
- これは、コードにいくつか不要、冗長、あるいは繰り返しがあるかもしれない兆候を示すコードの匂いだ
- パラメータとフィールドを組み合わせて一つに扱う、パラメトリックフィールドを定義することにって回避できる

```scala
    class ArrayElement(
      val contents: Array[String]
    ) extends Element
```

- `contents`パラメータの前に`val`が付いている
- これはフィールド名とパラメータ名を同時に同じ名前で定義する記法である
- 具体的には、`ArrayElement`はクラスの外からアクセス可能な`contents`フィールドを持つことになる
- このフィールドはパラメータの値で初期化される
- パラメータの前に`var`を付けることもできる
- この場合は、そのフィールドに値を再設定できる
- 結局、`private`や`protected`のような修飾子を付けることが可能である
- また、他のクラスメンバと同様、これらのパラメトリックフィールドをオーバーライドできる

```scala
 class Cat {
    val dangerous = false
  }
  class Tiger(
    override val dangerous: Boolean,
    private var age: Int
  ) extends Cat
```

- `Tiger`の定義は`オーバーライドされたdangerous`や`private`メンバである`age`を使って次のようにも定義できる

```scala
  class Tiger(param1: Boolean, param2: Int) extends Cat {
    override val dangerous = param1
    private var age = param2
  }
```

- この2つのメンバは指定されたパラメータで初期化される
- 我々はそれらの`param1`や`param2`といったパラメータの名前を適宜選ぶ
- 大事なことは他のどんな名前とも衝突しないことだ

# 10.7 Invoking superclass constructors

- 今、システムは`Element`クラスと`ArrayElement`クラスの2つのクラスから構成されている
- 別のelementの表現方法を検討したとする
- 例えば、クライアントは指定した文字列で1行を描画するレイアウト要素を欲しいとする
- その場合、単にサブクラスを追加することができる

```scala
  class LineElement(s: String) extends ArrayElement(Array(s)) {
    override def width = s.length
    override def height = 1
  }
```

- `LineElement`は`ArrayElement`を継承していて、`ArrayElement`のコンストラクタは`Array[String]`のパラメータを取るので、`LineElement`はスーパークラスのプライマリコンストラクタの引数を渡す必要がある
- スーパークラスのコンストラクタを呼び出す為には、スーパークラスの名前に続いて渡す引数を括弧に囲って記述するだけである
- 例えば、`LineElement`は`ArrayElement`のプライマリコンストラクタに渡す配列を`ArrayElement`の名前の後ろに括弧で囲って置く

```scala
  ... extends ArrayElement(Array(s)) ...
```

# 単語
- fulfill: 実現させる、満たす、果たす
- on track: 軌道に乗って、順調に進んで、再テストされて
- make sence: 意味をなす、道理にかなう、うなずける
- signify: ～を意味する、～を表す、示す
- reveal: 見せる、公開する、明らかにする
- liberal: 大まかな、寛大な、自由主義の、
- vice versa: 逆に
- conversely: 逆に、反対に
- as if: かのように
- uniformly: 均一に、一様に、滑らかに
- specifically: 具体的に