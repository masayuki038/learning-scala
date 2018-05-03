package test

import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")
import scala.collection.mutable.Set

object Solution2 {
  def solution(a: Array[Int]): Int = {
    val mutableSet = Set[Int]()
    a.foreach(mutableSet += _)
    1.to(mutableSet.size).foreach { i =>
      if (!mutableSet.contains(i)) {
        return i
      }
    }
    return mutableSet.size + 1
  }

  def main(args: Array[String]): Unit = {
    println(Solution2.solution(Array(1,2,3)))
  }
}

