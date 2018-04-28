package net.wrap_trap.learning_scala.chapter10.section8

import net.wrap_trap.learning_scala.chapter10.ArrayElement

class LineElement(s: String) extends ArrayElement(Array(s)){
  override def height = 1
  override def hidden(): Boolean = false
}
