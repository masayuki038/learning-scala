package net.wrap_trap.learning_scala.chapter3

/**
  * Created by masayuki on 2018/01/20.
  */
import scala.io.Source

object PrintEachLine {

  def main(args: Array[String]) = {
    def widthOfLength(s: String) = s.length.toString.length

    if (args.length > 0) {
      val lines = Source.fromFile(args(0)).getLines.toList

      val longestLine = lines.reduceLeft(
        (a, b) => if (a.length > b.length) a else b
      )
      val maxWidth = widthOfLength(longestLine)

      for (line <- lines) {
        val numSpaces = maxWidth - widthOfLength(line)
        val padding = " " * numSpaces
        println(padding + line.length + " | " + line)
      }
    } else {
      Console.err.println("Please enter filename")
    }
  }
}