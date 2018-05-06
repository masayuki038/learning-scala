package net.wrap_trap.learning_scala.chapter10.section14

import net.wrap_trap.learning_scala.chapter10.Element

object Main {
  def main(args: Array[String]): Unit = {
    val e1 = Element.elem(Array("++++++", "******"))
    val e2 = Element.elem("---")
    val e3 = Element.elem('/', 8, 2)

    println("above: ")
    print(e1.above(e2).above(e3))

    println("\nbeside: ")
    print(e1.beside(e2).beside(e3))
  }
}