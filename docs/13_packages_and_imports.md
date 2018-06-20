# 序章

- 特に大きなプログラムを書く時、結合度-他の部分に依存するプログラムの範囲-を小さくすることは重要だ
- 結合度が低いと、あるプログラムの部分の、小さな、一見無害な変更がもう一方の部分に壊滅的な結果をもたらすようなリスクを減らす
- 結合度を低くする一つの方法は、モジュラースタイルで書くことである
- あるプログラムを複数の小さなモジュールに分け、各々が内側と外側を持つ
- モジュールの内側-実装-の作業を行う時は、とても小さいモジュールの中で他のプログラマと協業するだけで良い
- モジュールの外側-インターフェース-を変更する時は、外部のモジュールの開発者と協業する必要がある
- この章は、あるモジュラースタイルでプログラムを構築するいくつかの方法を示す
- パッケージの配置の仕方、`import`を通しての名前の見せ方、アクセス修飾子の定義による可視範囲の制御を示す
- その構成はJavaの考え方に似ているが、いくつか違いもある
- 通常はより一貫性を持った形式となる
- すでにJavaを知っていてもこの章を読んでおいた方が良い

# 13.1 Packages

- ScalaのコードはJavaプラットフォームのグローバルなパッケージヒエラルキーに属する
- この本で見てきたサンプルコードは無名パッケージだった
- 名前付きのパッケージにコードを配置する方法が2つある
- 一つは、ファイルの上部にpackage節を書くことで、ファイル全体の内容をパッケージに入れることができる

```scala
    package bobsrockets.navigation
    class Navigator
```

- 上記のpackage節は`Navigator`を`bobsrockets.navigation`という名前のパッケージに配置する
- Note: ScalaのコードはJavaのエコシステムの一部なので、外部に公開するScalaパッケージとしてはJavaの`reverse-domain-name`形式を踏襲することを進める
- それゆえ、`Navigator`のパッケージのより良い名前は、`com.bobsrockets.navigation`だろう
- しかしながら、この章では、より理解しやすくする為に"com."を省略する
- Scalaにおいてコードをパッケージに配置するもう一つの方法は、C#の名前空間に似たやり方である
- パッケージ節に続き、パッケージに入れる定義をカーリー括弧で囲む
- とりわけ、この形式はファイルの一部を別のパッケージに入れることになる
- 例えば、本体のコードをテストするコードを同じファイルの別のパッケージとして入れることができる

```scala
    package bobsrockets {
      package navigation {

        // In package bobsrockets.navigation
        class Navigator

        package tests {

          // In package bobsrockets.navigation.tests
          class NavigatorSuite
        }
      }
    }
```

- 13.1で示したJavaライクな記法は、実際には13.2のようにより一般的な記法であるネスト形式のシンタックスシュガーでしかない
- 事実、そのパッケージにネストさせる場合、13.3のアプローチはインデントを重ねなくて済む

```scala
    package bobsrockets.navigation {

      // In package bobsrockets.navigation
      class Navigator

      package tests {

        // In package bobsrockets.navigation.tests
        class NavigatorSuite
      }
    }
```

- この記法のヒントは、Scalaのパッケージは本当にネストしてる、ということだ
- それは、navigationパッケージは意味的にbobsrocketsというパッケージの内側にある
- Javaパッケージは、階層的であるにもかかわらず、ネストしない
- Javaでは、パッケージに名前を付ける時は常に、パッケージ階層のルートから始めなければならない
- Scalaは言語をシンプルにする為により一般的なルールを使用している

# 単語

- extent: 範囲、規模、程度
- innocuous: 無害の、悪気のない、怒らせるつもりのない
- devastating: 荒廃させる、破壊的な、圧倒的な
- consequence: (続いて起こる、または必然的な)結果、成り行き、結果
- seemingly: 一見
- presumably: おそらく
- among other things: とりわけ
- semantically: 意味的に
- despite: ～にもかかわらず、～をよそに、～の意に反して