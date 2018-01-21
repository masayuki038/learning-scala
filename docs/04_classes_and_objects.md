# 序章
- ここまでの2つの章で、クラスとオブジェクトの基本は見てきた
- この章ではもう少し詳細に見ていく
- クラスやフィールド、メソッドについて学び、セミコロンインターフェースの概要を知ることができる
- さらにScalaのアプリケーションででシングルトンオブジェクトをどのように使うかを学ぶ
- Javaに馴染みがあれば、Scalaに同じようなコンセプトを見つけるだろうが、正確には同じではない
- Javaの達人だったとしても、読むに値する

# 4.1 Classes, fields, and methods
- クラスはオブジェクトの設計図である
- 一旦クラスを定義したら、`new`キーワードを使ってクラスからオブジェクトを作成することができる

```scala
class ChecksumAccumulator {
  // class definition goes here
}
```

```scala
new ChecksumAccumulator
```

- クラスの定義は、フィールドとメソッドの2つを持つ。それらはひとまとめにメンバと呼ばれる
- フィールドは、valやvarで定義され、オブジェクトを参照する
- メソッドはdefで定義され、実行可能なコードを持つ
- フィールドはオブジェクトの状態やデータを持ち、一方でメソッドはそのデータをオブジェクトの計算処理で使用する
- クラスのインスタンスを作ると、ランタイムはオブジェクトの状態を保存する為のメモリを別途確保する
- 例えば、ChecksumAccumulatorクラスにsumというフィールドを定義したとする

```scala
class ChecksumAccumulator {
  var sum = 0
}
```

- そしてこのクラスのオブジェクトを2つ生成する

```scala
val acc = new ChecksumAccumulator
val csa = new ChecksumAccumulator
```

- sumはChecksumAccumulatorのクラスでvarフィールドとして宣言されているので、後から別の値を割り当てることがでできる

```scala
acc.sum = 3
```

- それぞれのオブジェクトが自身の値セットを持つので、フィールドはインスタンス変数として知られている
- 2つのオブジェクトのうちの片方のフィールドを変更しても、もう片方のオブジェクトのフィールドの値には影響しない
- また、accはvalなので、accに別のオブジェクトを割り当てることができない

```scala
// Won't compile, because acc is a val
acc = new ChecksumAccumulator
```

- つまり、accは一度割り当てたら常に同じChecksumAccumulatorのオブジェクトを参照するが、フィールドは時間とともに変わる












































































