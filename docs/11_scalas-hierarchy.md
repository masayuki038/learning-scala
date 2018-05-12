# 序章

- Scalaのクラスヒエラルキーの話
- Anyはすべてのクラスのスーパークラスで、Anyに定義されているメソッドはユニバーサルメソッドである
- Nothingは他のすべてのクラスのサブクラスである

# 11.1 Scala's class hierarchy

- Anyには以下のメソッドが定義されている
  - final def ==(that: Any): Boolean
  - final def !=(that: Any): Boolean
  - def equals(that: Any): Boolean
  - def hashCode: Int
  - def toString: String

- `==`と`!=`はオーバーライドできない
- `==`は`equals`の結果と同じで、`!=`は`equals`の結果の逆になる
- 故に`==`や`!=`の振る舞いは、`equals`をオーバーライドすることで調整する
- Anyのサブクラスには`AnyVal`と`AnyRef`がある
- `AnyVal`はScalaのByte、Short、 Char、 Int、 Long、Float、Double、Boolean、Unitといった組み込みの値のスーパークラスである
- これらのクラスのインスタンスはリテラルで表現される
  - 例えば`Int`の42、`Char`の'x'、`Boolean`のfalseである
- これらのクラスnewできない
- これは、これらの値がabstractでfinalなクラスで定義されることによって実現している
- Unitは大まかにはJavaのvoidに相当する
- 値のクラスは、メソッドとして算術演算や真偽の演算子をサポートしている
- 例えば、`Int`は`+`や`*`をサポートしている。Boolanは`||`や`&&`をサポートしている。Anyのメソッドも継承している
- 値クラスはフラットである
- 全ての値クラスはAnyValのサブタイプであるが、値クラスのサブタイプではない
- そのかわり、異なる値クラスの型の間で`implicit conversion`が効く

```scala
  scala> 42 max 43
  res4: Int = 43

  scala> 42 min 43
  res5: Int = 42

  scala> 1 until 5
  res6: Range = Range(1, 2, 3, 4)

  scala> 1 to 5
  res7: Range.Inclusive = Range(1, 2, 3, 4, 5)

  scala> 3.abs
  res8: Int = 3

  scala> (-3).abs
  res9: Int = 3
```

- `min`、`max`、`until`、`to`、`and`、`abs`は`RichInt`のメソッドで、`implicit conversion`によって`Int`->`RichInt`に変換され、メソッドが実行される
- `AnyRef`は全ての参照クラスの基底クラスである
- `AnyRef`は、Javaの`java.lang.Object`に相当し、実際に変換できるので、JavaのObject相当の扱いは`AnyRef`を使うのがオススメ
- Scalaのクラスは、Javaとは違って`ScalaObject`というマーカートレイトを継承している
- これは、ScalaのコンパイラがScalaのプログラムをより効果的に実行する為の機能を持っている
- 現時点では、`ScalaObject`はパターンマッチのスピードアップの為に内部で使う`$tag`という名前のメソッドを持っているのみである

# 単語

- tailor: 服を仕立てる、合わせる、調整する