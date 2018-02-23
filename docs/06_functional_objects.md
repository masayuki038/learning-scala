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

# 6.7 Auxiliary constructors

- クラスに複数のコンストラクタが必要な場合がある
- Scalaでは、プライマリコンストラクタ以外のコンストラクタを「補助コンストラクタ」と呼ぶ
- 例えば、分母が1の有理数は単に分子だけを書くことによって簡潔にできる
- `new Rational(5, 1)`と書く代わりに、単に`new Rational(5)`と書くことができる
- これは、分母が1の時に分子だけを取る補助コンストラクタの追加が必要になる

```scala
class Rational(n: Int, d: Int) {
  require(d != 0)

  val numer: Int = n
  val denom: Int = d

  def this(n: Int) = this(n, 1) // auxiliary constructor

  override def toString = numer + "/" + denom

  def add(that: Rational): Rational =
    new Rational(
      numer * that.denom + that.numer * denom, denom * that.denom
    )
}
```

- Scalaでは補助コンストラクタは`this(...)`から始まる
- 補助コンストラクタの中身は、分子にその引数を、そして分母に1を指定し、プライマリコンストラクタを呼び出すだけである

```
scala> val y = new Rational(3)
y: Rational = 3/1
```

- Scalaでは、すべての補助コンストラクタはまず同じクラスの別のコンストラクタを呼び出すことになる
- 言い換えると、すべての補助クラスの最初のステートメントは`this(...)`となる
- 呼び出されたコンストラクタはプライマリコンストラクタか、あるいは呼び出し側のコンストラクタの前に提供されるもう一つの補助コンストラクタとなる
- このルールのネット効果は、Scalaにおけるすべてのコンストラクタの呼び出しは、最終的にプライマリコンストラクタの呼び出しになる
- それゆえ、プライマリコンストラクタはそのクラスの単一のエントリポイントとなる
- Note: Javaでは同じクラスの別のコンストラクタを呼び出すかsuperのコンストラクタを呼び出すが、Scalaではsuperのコンストラクタはプライマリコンストラクタだけが呼び出す
- Scalaにおけるその追加の制限は、Javaと比べてScalaのコンストラクタの短さと簡潔さを増大させる設計のトレードオフである

# 6.8 Private fields and methods

- 前のバージョンのRationalでは、分子nと分母dで単純に初期化した
- 結果として、Rationalの分子と分母は必要以上に大きくなることがある
- 例えば、66/42という分数は、正規化することで11/7にすることができる
- しかし、プライマリコンストラクタでは現在これを行っていない
- 正規化する為には、分子と分母の最大公約数で除算する必要がある
- 例えば、66と42の最大公約数は6である。66/42の分子と分母をそれぞれ6で除算すると、11/7になる

```scala
class Rational(n: Int, d: Int) {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  val numer = n / g
  val denom = d / g

  def this(n: Int) = this(n, 1)

  def add(that: Rational): Rational =
    new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

  override def toString = numer + "/" + denom

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
```

- このバージョンのRationalは、`g`というprivateフィールドと、numerとdenomの初期化を変更した
- `g`はprivateなので、クラスの内部からのみアクセス可能である
- 渡された2つのIntから最大公約数を求める`gcd`というメソッドも追加した
- privateなヘルパーメソッド`gcd`を追加した目的は、クラスの他の部分(この場合はプライマリコンストラクタ)で必要とされるコードを外に出した
- `gcd`に渡す`n`と`d`は絶対値なので、gは常に正の値であることを確認する
- Scalaコンパイラは、プライマリコンストラクタにて、ソースコードに現れた順で3つのフィールドを追加する書初期化コードを配置する
- `g`の初期化(`gcd(n.abs, d.abs)`)は、ソースの先頭に記載されているので、他の2つよりも前に実行される
- フィールド`g`は、結果として、`n`と`d`のそれぞれの絶対値から求められた最大公約数で初期化される
- それから、フィールド`g`はnumerやdenomの初期化で使われる
- nとdを最大公約数で除算することにより、Rationalは正規化されて構築される

```scala
scala> new Rational(66, 42)
res0: Rational = 11/7
```

# 6.9 Defining operators

- Rationalクラスに加算のメソッドを追加したが、より便利に使いたい

```scala
x + y
```

- もしxとyが整数や浮動小数だったとしても、以下のように書く必要がある

```scala
x.add(y)
```

- あるいは、少なくても以下のように

```scala
x add y
```

- もしそれらが有理数であればどうか
- そうするべきだという確証はない
- 有理数は他と同様に数値である
- 数学的には、有理数は浮動小数よりむしろ自然である
- なぜ普通の演算子を作るべきではないのか？Scalaはこれができる
- この最初のステップは、加算を演算子を使う形に置き換えることだ
- 率直に考えて、Scalaでは`+`が適切な演算子になる
- シンプルに`+`という名前のメソッドを定義する
- 同じように積算する`*`を実装することもできる

```scala
object Rational {
  def main(args: Array[String]) = {
    val x = new Rational(1, 2)
    val y = new Rational(2, 3)
    println("x + y: " + (x + y))
    println("x.+(y): " + x.+(y))
    println("x + x * y: " + (x + x * y))
    println("(x + x) * y: " + ((x + x) * y))
    println("x + (x * y): " + (x + (x * y)))
  }
}

class Rational(n: Int, d: Int) {

  require(d != 0)

  private val g = gcd(n.abs, d.abs)
  val numer = n / g
  val denom = d / g

  def this(n: Int) = this(n, 1)

  def + (that: Rational): Rational =
    new Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

  def * (that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  override def toString = numer +"/"+ denom

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
}
```

- その他に覚えておく点として、演算子の優先順位がある
- `*`演算子は`+`よりも結合が強い
- 言い換えると、Rationalの`+`と`*`の演算子は期待どおり動くだろう
- 例えば、`x + x * y`は`(x + x) * y`ではなく、`x + (x * y)`として実行される

```scala
x + y: 7/6
x.+(y): 7/6
x + x * y: 5/6
(x + x) * y: 2/3
x + (x * y): 5/6
```

# 6.10 Identifiers in Scala

- これまで、Scalaにおける識別子の形式は英数字と演算子の2つ見てきた
- Scalaは識別子の形式についてとても柔軟なルールを持っている
- これまで見てきた2つの形式に加えて、その他にも2つある
- 全4つの形式についてこのセクションで述べる
- 英数字の識別子は文字かアンダースコアから始まり、文字、数字、アンダースコアが続く
- しかしながら、Scalaコンパイラが生成する`$`は文字としてカウントされる
- コンパイルされるユーザプログラム側には`$`を含むべきではない
- そうした場合、Scalaコンパイラによって生成された識別子と名前の衝突が発生する可能性がある
- Scalaは、StringやHashSetのように、Javaにならってcamel-caseの識別子を使う
- アンダースコアは利用可能な識別子ではあるものの、普通Scalaプログラムでは使用されない。なぜなら、アンダースコアはScalaのコードにおいて識別子以外の使われ方をされることがある為である
- 結果として、`_string`や`__init__`や`name_`は避けるのがベストである
- フィールドやメソッドパラメータ、ローカル変数、関数のcamel-caseの名前は、例えば`length`や`flatMap`のように小文字で開始するべきである
- クラスの名前は`BigInt`や`List`、`UnbalancedMap`大文字から始めるべきである
- `val name_: Int = 1`と書くとコンパイルエラーになる。アンダースコアとコロンの間にスペースを入れ、`val name_ : Int = 1`とするべきである
- Javaとは違うScalaの規則の一例は定数の名前である
- Scalaでは、定数は`val`とは少し異なる
- `val`は初期化した後に変わらないものの、変数である
- 例として、メソッドパラメータが`val`だった場合でも、メソッドの呼び出しの度に`val`が保持する値は異なる
- 定数はより永続的である
- 例えば、`scala.math.Pi`はπの近似値(円周と直径の比)として定義されている
- この値は変更することができないので、`Pi`は明確に定数である
- 定数は、その他にも、マジックナンバーや説明の無いリテラル、最悪のケースとしてはあらゆるところに出現する
- パターンマッチングに使いたいこともあるだろう(Section 15.2で記載する)
- Javaでは定数は、`MAX_VALUE`や`PI`のように、すべて大文字で単語の栗切りにアンダースコアを使って記載していた
- Scalaでは最初の文字を単に大文字とするのみである
- それゆえ、Javaで`X_OFFSET`と記載していた定数は、Scalaでも動くものの、規則としてはcamel-caseを使ってはXOffsetと書く
- 演算識別子は一つ以上の演算子で成り立つ
- 演算子は例えば+、:、>、~あるいは#のようなものだ
- 演算識別子の例は以下のようなものである

```scala
+ ++ ::: <?> :_>
```

- Scalaコンパイラは内部で演算識別子に$を付けてJavaで認識できる識別子にバラす
- 例えば、`:_>`演算子は、内部では`$colon$minus$grater`になる
- もしこの識別子にJavaからアクセスしたい場合は、この内部表現を使う必要がある
- Scalaの演算識別子は任意の長さを取れるので、JavaとScalaにはちょっとした違いがある
- Javaでは、`x<-y`は4つのシンボルに分解されるので、`x < - y`と評価されるのと同等の意味になる
- Scalaでは、`<-`は一つの識別子として認識される
- 前者のように認識してもらいたい場合は、`<`と`-`の間にスペースを入れる
- これは実際の問題とは異なるが、Javaで`x<-y`と書く時に演算子間にスペースを入れる人はとても少ない
- 混合識別子は英数字とそれに続くアンダースコア、そして演算子から構成される
- 例えば、単項の加算を表す`unary_+`である
- あるいは、`myvar_=`という代入演算子を表すメソッド名がある
- 加えて、`myvar_=`はproperitesをサポートするためにScalaコンパイラによって生成される
- リテラル演算子はバックティックで囲まれる文字列である
- このアイデアは、バックティックで囲まれた文字列を実行時に識別子として認識させるようにする
- この結果は常にScalaの識別子となる
- これは名前にバックティックが入っている名前がScalaの予約語だとしても動く
- 典型的なユースケースはは、Javaのスレッドクラスの静的なyieldメソッドにアクセスすることである
- Scalaでは`yield`が予約語なので、`Thread.yield()`と書くことはできないが、Thread.``yield``()と書くことはできる

# 単語
- factor out: 追い出す
- depart: 出発する、起動から逸れる、規制から逸脱する
- diameter: 直径
- circumference: 直径
- mangle: ずたずたにする、台無しにする、しわ伸ばし機
- arbitrarily: 任意に、気ままに、独断的に
