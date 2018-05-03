package test

import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")
import scala.collection.mutable.Set

object Solution4 {
  def solution(a: Int, b: Int, c: Int, d: Int): String = {
    try {
      val digits = scala.collection.mutable.ListBuffer(a, b, c, d)
      val t1 = digits.filter(i => i <= 2).max
      if (!digits.contains(t1)) {
        return "NOT POSSIBLE"
      }
      digits -= t1

      val t2 = t1 match {
        case 2 => digits.filter(i => i <= 3).max
        case _ => digits.max
      }
      if (!digits.contains(t2)) {
        return "NOT POSSIBLE"
      }
      digits -= t2

      val t3 = digits.filter(i => i <= 5).max
      if (!digits.contains(t3)) {
        return "NOT POSSIBLE"
      }
      digits -= t3
      return "%d%d:%d%d".format(t1, t2, t3, digits(0))
    } catch { case e: UnsupportedOperationException =>
      return "NOT POSSIBLE"
    }
  }

  def main(args: Array[String]): Unit = {
    println(Solution4.solution(1, 8, 3, 2))
    println(Solution4.solution(2, 4, 0, 0))
    println(Solution4.solution(3, 0, 7, 0))
    println(Solution4.solution(9, 1, 9, 7))
  }
}

