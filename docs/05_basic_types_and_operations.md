# 序章
- これまでクラスやオブジェクトを見てきたので、Scalaの基本的な型や操作をより深く見ていく良い頃合いである
- Scalaの基本的な型と操作はJavaに似ている
- しかしながら、いくつかの違いはあるので、Javaデベロッパーといえどもこの章を読む価値がある
- この章がカバーするScalaの側面のいくつかは本質的にJavaと同じだが、Javaデベロッパーが読み飛ばしやすいように、いくつかのメモを入れた
- この章ではScalaの基本的な型や、操作、演算子の優先順位を学ぶ
- さらに、Scalaのimplicit conversionが基本的な型の変異をいかに豊かにするかを学ぶ

# 5.1 Some basic type
- Scalaの基本的な型が取りうふ範囲は以下のとおり

|型|定義|範囲|
|:-----------|:------------|:------------|
|Byte|符号付き8ビット整数|-2^7 から2^7-1|
|Short|符号付き16ビット整数|-2^15から2^15-1|
|Int|符号付き32ビット整数|-2^31から2^31-1|
|Long|符号付き64ビット整数|-2^63から2^63-1|
|Char|符号無し16ビットUnicode文字|0から2^16-1|
|String|文字列||
|Float|32ビットIEEE754単精度浮動小数点数||
|Double|64ビットIEEE754倍精度浮動小数点数||
|Boolean|trueあるいはfalse||

- String以外の上記の型はscalaパッケージのメンバである(Stringはjava.lang.String)
- 例えば、Intの完全な名前はscala.Intである
- しかしながら、scalaのソースファイルにおいて、scalaパッケージとjava.langパッケージは自動的にimportされるので、パッケージ名を入れないシンプルな名前で使うことができる
- Note: Scalaでは、Javaのプリミティブ型に合わせて、型名を小文字にしたaliasを使うことができる
- 例えば、ScalaのプログラムではIntの代わりにintを使うことができる
- どちらもscala.Intであることを気に留めておく必要がある
- Scalaコミュニティの慣習から、この本ではIntを使っており、Intを使うことを推奨する
- 小文字の型は将来非推奨となり削除される可能性すらある
- 手練のJavaデベロッパーなら、Scalaの基本的な型が取りうる値が、Javaの各型が取りうる範囲と同じであることに気がつくだろう
- これにより、ScalaのコンパイラがByteコードを生成する際にIntやDoubleなどの値をJavaのプリミティブ型に変換することができる

# 5.2 Literals
- 基本的な型はリテラルを書くことができる
- リテラルは固定値を直接コードに書く方法である
- ほとんどのリテラルの書き方はJavaと同様なので、Javaマスターであればこのセクションはスキップして構わない
- Javaマスターが知るべき2つの違いは、文字列とシンボルのリテラルである

## Integer literals
- Int, Long, Short, Byteの整数リテラルは、10進数、16進数、8進数の3つの形式を取る
- 整数リテラルは数字から始まる
- 0xや0Xから始まる場合は16進数で、0-9とA-Fの文字を取る

```scala
scala> val hex = 0x5
hex: Int = 5

scala> val hex2 = 0x00FF
hex2: Int = 255

scala> val magic = 0xcafebabe
magic: Int = -889275714
```

- Scalaシェルは10進で値を出力するが、リテラルの記載の仕方は気にしなくて良い
- それゆえインタプリタは"0x00FF"で初期化されたhex2の値を255として表示する
- 0から始まる場合は8進数で、0-7の文字を取る
  - が、2.11で実際に試してみるとエラーになる

```scala
scala> val oct = 035
<console>:1: error: Decimal integer literals may not have a leading zero. (Octal
 syntax is obsolete.)
val oct = 035
```

- 0以外の数字で始まると10進数になる

```scala
scala> val dec1 = 31
dec1: Int = 31

scala> val dec2 = 255
dec2: Int = 255

scala> val dec3 = 20
dec3: Int = 20
```

- 整数の値がLかlで終わっている場合はLongで、それ以外はIntになる

```scala
scala> val prog = 0XCAFEBABEL
prog: Long = 3405691582

scala> val tower = 35L
tower: Long = 35

scala> val of = 31l
of: Long = 31
```

- 整数値をShortやByt型の変数に割り当てた時、リテラル値がその型の有効な範囲内にある限り、ShortまたはByteとして扱われる

```scala
scala> val little: Short = 367
little: Short = 367

scala> val littler: Byte = 38
littler: Byte = 38
```

## Floating point literals

- 浮動小数点リテラルは数字と小数点で構成され、且つEかeの後に、指数が表記されることがある

```scala
scala> val big = 1.2345
big: Double = 1.2345

scala> val bigger = 1.2345e1
bigger: Double = 12.345

scala> val biggerStill = 123E45
biggerStill: Double = 1.23E47
```

- 指数部は、10のべき乗の数を示し、その結果はその他の部分と掛け合わせられる
- それゆえ、1.2345e1は1.2345*10^1になる
- 浮動小数リテラルの最後がFまたはfの場合はFloat、それ以外はDoubleとなる

```scala
scala> val littele = 1.2345F
littele: Float = 1.2345

scala> val littleBigger = 3e5f
littleBigger: Float = 300000.0
```

- また、Doubleの浮動小数点リテラルはDまたはdでも書ける

```scala
scala> val anotherDouble = 3e5
anotherDouble: Double = 300000.0

scala> val yetanother = 3e5D
yetanother: Double = 300000.0
```

## Character literals

- 文字リテラルはシングルクォートの間にUnicodeの文字が入る形式である

```scala
scala> val a = 'A'
a: Char = A
```

- 加えて、シングルクォートの間には、バックスラッシュの後に8進や16進のコードポイントを指定することができる
- この8進の値は"\0"から"\377"の間になる
- 例えば、UnicodeのAのコードポイントは8進で101である

```scala
scala> val c = '\101'
warning: there was one deprecation warning; re-run with -deprecation for details
c: Char = A

scala> val d = '\u0041'
d: Char = A

scala> val d = '\u0044'
d: Char = D
```

- 実際、Scalaプログラムの色んなところに出現する
- 例えば、このように識別子にも書くことができる

```scala
scala> val B\u0041\u0044 = 1
BAD: Int = 1
```

- 上記コードの2つのUnicode文字は、BADと認識される
- 一般的に、このような名前の付け方は読みにくいので良くない
- 最後に、以下のように特別なエスケープシーケンスによって表現される文字もある

```scala
scala> val backslash = '\\'
backslash: Char = \
```

|リテラル|意味|
|:-----------|:------------|
|\n|line feed(\u000A)|
|\b|backspace(\u0008)|
|\t|tab(\0009)|
|\f|form feed(\u000C)|
|\r|carriage return(\u000D)|
|\"|double quote(\u0022)|
|\'|single quote(\u0027)|
|\\|backslash(\u005C)|

## String literals

- Stringリテラルはダブルクォートで囲んだ文字列で構成される

```scala
scala> val hello = "hello"
hello: String = hello
```

- クォート内の文字列の書式はCharacterリテラルと同様である

```scala
scala> val escape = "\\\"\'"
escape: String = \"'
```

- この書式は沢山のエスケープシーケンスがある文字列や複数行に渡る文字列を書きにくいので、SpalaにはRaw Stringという特別な書式がある
- この文字列は3つのダブルクォート(""")を記載して開始、終了を表す
- この文字列の秘密は、改行、クォーテーション、特殊文字等何でも含む(但し"""は除く)

```scala
scala> println("""Welcome to Ultamix 3000.
     | Type "HELP" for help.""")
Welcome to Ultamix 3000.
Type "HELP" for help.
```

- この問題は、2行目の先頭にスペースが入ってしまうことだ
- 一般的な解決方法は、stripMarginメソッドを使うことだ
  - とあるけど、2.11では先頭に改行が入らない

## Symbol literals

- シンボルリテラルは'同一性'で、'同一性'は英数字の識別子である
- このリテラルは事前に定義されたscala.Symbolのインスタンスにマッピングされる
- 具体的には、'cymbalリテラルはSymble('cymbal)というファクトリーメソッドを起動する
- シンボルは動的型付言語で識別子として使用される
- 例えば、データベースのレコードをupdateするメソッドを定義したい場合、

```
scala> def updateRecordByName(r: Symbol, value: Any){}
updateRecordByName: (r: Symbol, value: Any)Unit
```

- このメソッドはレコードの名前を示すシンボルをパラメータとして取る

```scala
updateRecordByName('favoriteAlbum, "OK Computer")
```

- シンボルでできることはほとんどないが、名前を見ることはできる

```scala
scala> val s = 'aSymbol
s: Symbol = 'aSymbol

scala> s.name
res1: String = aSymbol
```

- シンボルはinternされている
- もし同じシンボルを2回書いても、両方とも同じシンボルオブジェクトを参照する

## Boolean literal

- ブーリアン型は、true / falseの2つのリテラルを取る

```scala
scala> val bool  = true
bool: Boolean = true

scala> val fool = false
fool: Boolean = false
```

# 5.3 Operators are methods

- Scalaは基本型に対して強力な演算子を提供する
- 前章で触れたとおり、これらの演算子は実際には通常のメソッドコールの一つにすぎない
- 例えば、1 + 2は実際には(1).+(2)と同じ意味になる
- 言い換えると、IntクラスはIntの引数を一つとりIntの結果を返す、+というメソッドを持っている
- 明示的に呼び出すと以下のようになる

```scala
scala> val sumMore = (1).+(2)
sumMore: Int = 3
```

- 実際には、Intは+メソッドについて、異なる型を取るいくつかのオーバーロードを持っている
- 例えば、Longを取り、Longを返すIntメソッドがある
- もしIntの値にLongの値を加えた時に、このメソッドは起動する

```scala
scala> val longSum = 1 + 2L
longSum: Long = 3
```

- 演算子の記法は、他の言語と同様に+のようなメソッドに限定されない
- どんなメソッドにもオペレータ記法を使うことができる
- 例えば、StringにはCharacterを1つとるindexOfメソッドがある。これを演算子のように呼び出すと以下のようになる

```scala
scala> val s = "Hello, world!"
s: String = Hello, world!

scala> s indexOf 'o'
res0: Int = 4
```

- indexOfには、2つのパラメータを取るメソッドもある
- 演算子の記法で複数の引数があるメソッドを呼び出す場合は、引数を括弧でくくる必要がある

```scala
scala> s indexOf ('o', 5)
res1: Int = 8
```

- さらに、Scalaには2つの演算子の記法がある。一つはprefix、もう一つはpostfixだ
- prefix記法は、メソッドを起動するオブジェクトの前にメソッド名を配置する方法で、`-7`の'-'がこれにあたる
- postfix記法は、オブジェクトの後にメソッド名を配置する方法で、`7 toLong`の'toLong'がこれにあたる

