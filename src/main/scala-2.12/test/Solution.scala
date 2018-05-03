package test

import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")
import scala.collection.mutable.Set

object Solution {
  def solution(s: String): Int = {
    compute(s, s.length / 2)
  }

  def compute(s: String, i: Int): Int = {
    val left = s.substring(0, i)
    val right = s.substring(i)
    println("right: " + right)
    val diff = count(left, '(') - count(right, ')')
    if (diff == 0) return i else compute(s, i - diff)
  }

  private def count(s: String, p: Char):Int = {
    var count = 0
    for (c <- s) {
      if (c == p) {
        count += 1
      }
    }
    return count
  }

  def main(args: Array[String]): Unit = {
    println(Solution.solution("(()))()()()(()))(())))()()()"))
  }
}

