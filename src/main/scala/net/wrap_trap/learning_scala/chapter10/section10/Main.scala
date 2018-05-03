package net.wrap_trap.learning_scala.chapter10.section10

import net.wrap_trap.learning_scala.chapter10.ArrayElement

object Main {
  def main(args: Array[String]): Unit = {
    val e1 = new ArrayElement(Array("+++++", "*****"))
    val e2 = new ArrayElement(Array("-----", "/////"))
    println("above: ")
    e1.above(e2).contents.foreach(println)

    println("\nbeside: ")
    e1.beside(e2).contents.foreach(println)
  }
}
