# 序章
- Scalaの本格的なチュートリアルを始める前に、Scalaの雰囲気を味わう為に2つの章を用意した
- そして重要なことは、コードを書くことだ
- この章のすべてのコードを試してから次に進むことを推奨する
- Scalaを学び始める一番良い方法は、Scalaのプログラムを書くことだ
- ベテランのあなたがScalaに入門するのであれば、次の章はScalaで有用なコードを書き始めることができるようになる為に十分であろう
- 経験の少ないあなたには、少し不思議に見えるかもしれない
- 心配はいらない。素早く学ぶ為に、詳細を省いている
- すべては"消化ホース"のスタイルで後段の章で説明される
- 我々は次の2つの章に、より詳細な説明がある本のセクションを示した注釈を入れた

# Step 1. Learn to use the Scala interpreter
- Scalaを始める最も簡単な方法は、Scalaの記法やプログラムを書くためのシェルであるScalaインタプリタを開始することだ
- インタプリタにコードを書くと、評価されて結果が表示される
- このシェルは単にscalaと呼ばれる
- '1+2'のようにタイプしてエンターキーを押すと、

```
scala> 1 + 2
```

- インタープリタは以下のように出力する

```scala
res0: Int = 3
```

- これは以下のことを示す
  - 計算された結果を保存する変数は自動的に生成されるかユーザが定義できる(res0はResult 0の意味)
  - コロン(:)の後に型の名前が続く(Int)
  - イコール記号(=)
  - 値は計算式が評価された結果である

- Int型はscalaパッケージのIntクラスの名前である
- ScalaのパッケージはJavaのパッケージに似ている
- グローバルな名前空間のパーティションで、情報隠蔽(フルパッケージ名を記載しないと意味で)の仕組みを提供する
- Intクラスの値はJavaのintの値に対応する
- もう少し広げて言うと、Javaのすべてのプリミティブ型はScala側のクラスに対応がある
- 例えばScalaのBooleanはJavaのBooleanに、scalaのFloatはJavaのfloatに対応する
- さらに、ScalaのコードがJavaのバイトコードに変換される際、Scalaのコンパイラはプリミティブ型のパフォーマンスの恩恵を得る為に、Javaのプリミティブ型を使う
- resXは以下のように利用できる。例えば、前回res0には3が設定されているので、res0 * 3は9になる

```scala
scala> res0 * 3
res1: Int = 9
```

- 出力する必要があれば、"Hello, world! greeting"とタイプすれば良い

```scala
scala> println("Hello, world!")
Hello, world!
```

println関数は、JavaのSystem.out.printlnに似ていて、標準出力に文字列を出力する

# Step 2. Define some variables
- Scalaの変数には2種類ある
- 一つはJavaのfinalの変数と同様に値の再割り当てできないval
- もう一つはJavaのfinal無し変数と同様に値の再割り当てができるvar

```scala
scala> val msg = "Hello, world!"
msg: String = Hello, world!
```

- このステートメントはmsgという名前の変数に"Hello, world!"という文字列を割り当てる
- Scalaの文字列の実装はJavaのStringクラスなので、msgの型はjava.lang.Stringである
- Javaで変数宣言をしたことがあれば、val宣言のどこにもjava.lang.StringやStringがない、という違いに気が付くだろう
- この型推論の例が示すとおり、Scalaは型の指定を省略することができる
- このケースでは、文字列リテラルでmsgを初期化したので、Scalaはmsgの型がStringであると推測した
- Scalaが型を推測する時、不要に型を明記するよりもベストな選択をする
- しかしながら、型を明記することもできる
- 型の明記はScalaのコンパイラはあなたの意図の型を推測するだけでなく、将来コードを読む人にとっての有用なドキュメントになる
- Javaとは対照的に、Scalaは変数名の後にコロンで区切って型を指定する

```scala
scala> val msg2: java.lang.String = "Hello again, world!"
msg2: String = Hello again, world!
```

- あるいは、java.langの型はScalaプログラムでは省略できる

```scala
scala> val msg3: String = "Hello yet again, world!"
msg3: String = Hello yet again, world!
```

- 最初のmsgに戻り、定義した変数は適宜使うことができる

```scala
scala> print(msg)
Hello, world!
```

- msgはvalで宣言したので、値を再定義できない

```scala
scala> msg = "Goodby cruel world"
<console>:11: error: reassignment to val
       msg = "Goodby cruel world"
```

- varで宣言すれば値を再定義できる

```scala
scala> var greeting = "Hello, world!"
greeting: String = Hello, world!

scala> greeting = "Leave me alone, world!"
greeting: String = Leave me alone, world!
```

- 複数行を入力したい場合は、最初の行の後に続けてタイプする
- もしコードの入力が終わってなければ、インタープリタは次の行に|を表示する

```scala
scala> val multiline =
     | "This is the next line."
multiline: String = This is the next line.
```

- あなたの入力に何か間違いがあっても、enterが2回押されるまでは、インタープリタはさらに入力を待ち続ける

```scala
scala> val oops =
     |
     |
You typed two blank lines.  Starting a new command.
```

- 本書では以降、コードを読みやすくする為に|は省略する(インタープリタにコピペしやすいように)

























