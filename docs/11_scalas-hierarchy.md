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

# 単語

- tailor: 服を仕立てる、合わせる、調整する