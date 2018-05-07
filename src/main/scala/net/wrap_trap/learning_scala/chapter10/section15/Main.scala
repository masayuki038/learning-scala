package net.wrap_trap.learning_scala.chapter10.section15

import net.wrap_trap.learning_scala.chapter10.Element

object Main {
  val space = Element.elem(" ")
  val corner = Element.elem("+")

  def spiral(nEdges: Int, direction: Int): Element = {
    if (nEdges == 1)
      Element.elem("+")
    else {
      val sp = spiral(nEdges - 1, (direction + 3) % 4)
      def verticalBar = Element.elem('|', 1, sp.height)
      def horizontalBar = Element.elem('-', sp.width, 1)
      if (direction == 0)
        (corner beside horizontalBar) above (sp beside space)
      else if (direction == 1)
        (sp above space) beside (corner above verticalBar)
      else if (direction == 2)
        (space beside sp) above (horizontalBar beside corner)
      else
        (verticalBar above corner) beside (space above sp)
    }
  }

  def main(args: Array[String]): Unit = {
    println(spiral(17, 0))
  }
}