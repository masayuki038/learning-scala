package test

import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")
import scala.collection.mutable.Set

object Solution3 {
  def solution(a: Int, b: Int): Int = {

    if (a > 100000000 || b > 100000000) {
      return -1
    }

    val strA = a.toString
    val strB = b.toString
    val lenA = strA.length
    val lenB = strB.length
    val len = if (lenA > lenB) lenA else lenB

    val buf = new StringBuilder
    0.to(len).foreach { i =>
      if (i < lenA) {
        buf.append(strA.charAt(i))
      }
      if (i < lenB) {
        buf.append(strB.charAt(i))
      }
    }

    return buf.toString().toInt
  }

  def main(args: Array[String]): Unit = {
    println(Solution3.solution(12345, 678))
  }
}

