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






