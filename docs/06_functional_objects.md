# 序章

- 前章でScalaの基本を理解したので、Scalaですべての機能を持ったクラスをデザインする準備ができた
- この章の重点は、どんなmutableな状態も持たない関数オブジェクトを定義するクラスにある
- 例として、有理数をimmutableオブジェクトとしてモデリングするクラスのパターンをいくつか作成する
- この方法に沿って、クラスパラメータやコンストラクタ、メソッド、演算子、privateメンバ、オーバーライド、事前チェック、オーバーロード、そして自身への参照といった、Scalaのオブジェクト指向のより多くの側面を見ることができる

# 6.1 A specification for class Rational

- 有理数は、n/dで率を表現する数字であり、nとdは整数で、dは0以外になる
- nは分子、dは分母と呼ばれる
- 有理数の例は次のとおり: 1/2、2/3、112/239、2/1
- 浮動小数と比較すると、端数が有理数丸めや近似値を用いることなく正確な値で表現されるという利点がある
- この章でデザインするクラスは、加算、減算、積算、除算を含む有理数の振る舞いをモデル化する
- 2つの有理数を加算する場合、共通の分母を求めてから分子を加算する
- 積算は単純に分子と分母をそれぞれ掛けるだけ
- 除算は右項の分子と分母を逆転させて積算する
- 一つの観点としては、数学では有理数は状態を持たない
- 一つの有理数をもう一つの有理数と加算すると、結果は新しい有理数になる
- 元の数は変化しない
- この章でデザインするimmutableなRationalクラスは同じ性質を持つ
- それぞれの有理数は一つのRationalオブジェクトによって表現される
- 2つのRationalオブジェクトを加算すると、加算した値を持つ一つのRationalオブジェクトが生成される
- この章では、Scalaのネイティブな言語サポートであるかのようなライブラリを書くことができるのを垣間見るだろう

# 6.2 Constructing a Rational

- Rationalクラスのデザインを始める良い取っ掛かりは、クライアントプログラマにどのようにRationalオブジェクトを作ってもらうかを考えることだ
- Rationalオブジェクトはimmutableにすることにしたので、インスタンス生成時に、クライアントがインスタンスのすべてのデータを要求する
- それゆえ、以下のようになる

```scala
class Rational(n: Int, d: Int)
```

- 特筆するべき最初の点は、メソッドの中身が無ければ、空のカーリー括弧を指定する必要がない(付けたいと思えば付けることもできる)
- Rationalのクラス名の後のnとdは、クラスパラメータと呼ばれる
- Scalaコンパイラはこれらの2つのクラスパラメータを集約し、二つのパラメータを指定するプライマリコンストラクタを作成する
- このRationalの初期の例はJavaとScalaの違いを浮き彫りにする
- Javaでは、クラスはコンストラクタをを持つのに対して、Scalaはパラメータを直接取る
- Scalaの記法は、より短く書ける - クラスパラメータはクラスの本体に直接使用される
- フィールドを定義したり、フィールドに値を設定する為の記述が不要である
- これは、特に小さなクラスにおいて、お決まりのコードの記述を大幅に削減する
- Scalaコンパイラは、フィールドやメソッド以外の部分に記載されたコードをプライマリコンストラクタとしてコンパイルする

```scala
class Rational(n: Int, d: Int) {
  println("Created " + n + "/" + d)
}
```

- このコードにより、printlnの呼び出しはRationalのプライマリコンストラクタに配置する
- それゆえ、printlnの呼び出しは、Rationalインスタンスを構築する際にデバッグメッセージを出力する

# 6.3 Reimplementing the toString method

- 先の例で、Rationalオブジェクトを作成した時、"Rational@90110a"と表示された
- インタープリタはRationalオブジェクトのtoStringメソッドを呼び出した際、幾分奇妙な文字列を取得した
- デフォルトでは、Rationalクラスは、クラス名と@記号、そして16進数を出力するjava.lang.ObjectのtoStringの実装を継承している
- toStringの結果は、基本的にはデバッグやログメッセージ、テスト失敗のレポート、デバッガやインタープリタのアウトプットで使用可能な情報をプロジェクトに提供することを意図している
- toStringによって提供されるこの結果は、特別参考にならない、なぜなら、有理数の値について手がかりを提供しない為である
- より効果的なtoStringの実装は、有理数の分子と分母を表示することである
- RationalクラスのtoStringメソッドをオーバーライドすることができる

```scala
class Rational(n: Int, d: Int) {
  override def toString = n + "/" + d
}
```

- 先頭のoverride識別子は以前定義されたメソッド定義を上書きする、というシグナルである
- 有理数の表示が良くなったので、Rationalクラスに記述していたデバッグ出力のステートメントを削除した

```scala
scala> class Rational(n: Int, d: Int) {
     | override def toString = n +"/"+ d
     | }
defined class Rational

scala> val x = new Rational(1, 3)
x: Rational = 1/3

scala> val y = new Rational(5, 7)
y: Rational = 5/7
```

# 6.4 Checking preconditions

- 次のステップとして、プライマリコンストラクタの現状の振る舞いの問題に注目する
- この章の最初に述べたように、有理数は分母に0を取ることはない
- しかしながら、現在、プライマリコンストラクタはdの値に対して0を渡すことができる

```scala
scala> class Rational(n: Int, d: Int) {
     | override def toString = n +"/"+ d
     | }
defined class Rational

scala> new Rational(5, 0)
res0: Rational = 5/0
```

- オブジェクト指向プログラミングの恩恵の一つは、オブジェクトの内側にデータを閉じ込めることができる。なので、データが一貫して正しいことを確認することができる
- Rationalのようなimmutableオブジェクトのケースでは、これはオブジェクトが構築された時にデータが正しいことを確認するべきであることを意味する
- 分母に0を与えるということは、有理数としては不正なので、dに0が与えられた時は、Rationalを構築されるようにするべきではない
- この問題のベストな対応は、dが0以外の数値となるかどうか事前確認を入れることである
- この事前確認は、メソッドやコンストラクタに与えられた値に対する、呼び出し側がすべて満たすべき制約となる

```scala
scala> class Rational(n: Int, d: Int) {
     | require(d != 0)
     | override def toString = n +"/"+ d
     | }
defined class Rational
```

- requireメソッドは1つのbooleanパラメータを取る
- trueを与えると、普通に制御が戻る
- それ以外は、IllegalArgumentExceptionをスローしてオブジェクトの構築を止める

# 6.5 Adding fields

- プライマリコンストラクタは事前条件を適切に強制するようになったので、その先の事項に目を向ける
- その為には、Rationalをパラメータとして取るaddという名のpublicメソッドをRationalクラスに定義する
- Rationalをimmutableに保つ為には、addメソッドはそのレシーバに値を加算してはならない
- 加算結果を持った新しいRationalオブジェクトを作成して返すべきだ

```scala
class Rational(n: Int, d: Int) { // This won't compile
  required(d != 0)
  override def toString = n + "/" + d
  def add(that: Rational): Rational = new Rational(n * that.d + that.n * d, d * that.d)
}
```

- しかし、これはコンパイルが失敗する

```
<console>:11: error: not found: value required
       required(d != 0)
       ^
<console>:13: error: value d is not a member of Rational
       def add(that: Rational): Rational = new Rational(n * that.d + that.n * d,
 d * that.d)
                                                                 ^
<console>:13: error: value n is not a member of Rational
       def add(that: Rational): Rational = new Rational(n * that.d + that.n * d,
 d * that.d)
                                                                          ^
<console>:13: error: value d is not a member of Rational
       def add(that: Rational): Rational = new Rational(n * that.d + that.n * d,
 d * that.d)
 ```

- クラスパラメータ、nとdは、addメソッドのコードのスコープ内にあるけれでも、この値にアクセスできるのはaddメソッドを起動したオブジェクトのものだけである
- 結果として、addの実装でnやdに関しては、コンパイラはこれらのクラスパラメータの為の値を提供すると幸せになる
- しかし、that.nやthat.dと指定することはできない。なぜなら、それはaddを起動したRationalオブジェクトを参照しないので
- 分子と分母の値を参照する為には、それらをフィールドにする必要がある

```scala
class Rational(n: Int, d: Int) {
  require(d != 0)
  val numer: Int = n
  val denom: Int = d
  override def toString = numer + "/" + denom
  def add(that: Rational): Rational = new Rational(numer * that.denom + that.numer * denom, denom * that.denom)
}
```

```scala
scala> val oneHalf = new Rational(1, 2)
oneHalf: Rational = 1/2

scala> val twoThirds = new Rational(2, 3)
twoThirds: Rational = 2/3

scala> oneHalf add twoThirds
res0: Rational = 7/6
```

- もう一つの事項として、以前オブジェクトの外からアクセスできなかった分子や分母にアクセスすることができるようになった

```scala
scala> val r = new Rational(1, 2)
r: Rational = 1/2

scala> r.numer
res1: Int = 1

scala> r.denom
res2: Int = 2
```

# 6.6 Self references

- `this`というキーワードは、現在実行中のメソッドが呼び出されたオブジェクトインスタンスへの参照である
- あるいは、コンストラクタの中で使われている場合は、構築されたオブジェクトインスタンスへの参照である

```scala
def lessThan(that: Rational) =
  this.numer * that.denom < that.numer * this.denom
```

- ここでは、`this.numer`は`lessThan`が呼び出されたオブジェクトの分子を参照する
- プレフィックスを省略して`numer`と書くこともできる。2つの記法は同じ意味である
- `this`の省略をできない例として、Rationalクラスに、指定された有理数と引数で大きい方を返すmaxというメソッドを追加してみる

```scala
def max(that: Rational) =
  if (this.lessThan(that)) that else this
```

- ここでは、最初の`this`は冗長である
- しかし条件文がfalseを返した時に返す2つ目の`this`は省略することができない
