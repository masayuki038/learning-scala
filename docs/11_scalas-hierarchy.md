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

# 11.2 How primitives are implemented

- ScalaはJavaと同様に整数を32-bitで保存する
- これはJVMに対して効果的であり、Javaライブラリとの相互互換性があるという点でも重要である
- 加算、積算等の標準的な演算は、プリミティブ演算として行う
- しかしながら、整数がJavaのObjectとして必要な場合は、バックアップとして`java.lang.Integer`を使う
- これは整数に対して`toString`が呼ばれた時や、Any型の変数に整数を束縛する時に起きる
- `Int`の整数値のは必要な時に`java.lang.Integer`型に透過的に変換される

```java
  // This is Java
  boolean isEqual(int x, int y) {
    return x == y;
  }

  boolean isEqual(Integer x, Integer y) {
    return x == y;
  }
  System.out.println(isEqual(421, 421));
```

- 実際、上記例で`isEqual`を呼び出した際に返ってくる値は`false`である
- これは、`421`が2回boxingされ、`x`と`y`が別々のオブジェクトになっている
- `==`は参照先が同じであることを意味し、`Integer`オブジェクトの参照が異なるので、`false`が返る
- プリミティブと参照型で明確な違いがある
- 実際、Scalaの等価演算`==`は型表現に対して透過的になるように設計されている
- 値型においては、数値や真偽といった自然な等価比較となっている
- 参照型の場合、`==`はObjectから継承した`equals`メソッドのエイリアスとして扱われる
- そのメソッドはもともと参照の等価性を定義されていたが、それらの自然な等価を実装した多数のサブクラスにオーバーライドされている
- これは、ScalaはJavaでありがちな文字列等価のトラップに引っかかることが決して無いことを意味する

```java
  scala> val x = "abcd".substring(2)
  x: java.lang.String = cd

  scala> val y = "abcd".substring(2)
  y: java.lang.String = cd

  scala> x == y
  res12: Boolean = true
```

- Javaでは、`x`と`y`の比較は`false`になる
- プログラマは`equals`を使うべきところだが、簡単に忘れる
- しかしながら、ユーザ定義の等価よりも、参照の等価が必要なケースはある
- 例えば、最高に効果的ないくつかのシチュエーションの例として、いくつかのクラスの`hash cons`とインスタンスの参照の等価を比較がある
- これらのケースでは、`AnyRef`クラスに、参照を比較するオーバーライドできない`eq`メソッドがある

```scala
  scala> val x = new String("abc")
  x: java.lang.String = abc

  scala> val y = new String("abc")
  y: java.lang.String = abc

  scala> x == y
  res13: Boolean = true

  scala> x eq y
  res14: Boolean = false

  scala> x ne y
  res15: Boolean = true
```

# 単語

- tailor: 服を仕立てる、合わせる、調整する