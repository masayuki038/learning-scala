package net.wrap_trap.learning_scala.chapter10.section9

abstract class Element {
  def demo(): Unit = {
    println("Element's implementation invoked")
  }
}

class ArrayElement extends Element {
  override def demo(): Unit = {
    println("ArrayElement's implementation invoked")
  }
}

class LineElement extends ArrayElement {
  override def demo(): Unit = {
    println("LineElement's implementation invoked")
  }
}

class UniformElement extends Element {
  override def demo(): Unit ={
    println("UniformElement's implementation invoked")
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    invokeDemo(new ArrayElement())
    invokeDemo(new LineElement())
    invokeDemo(new UniformElement())
  }

  def invokeDemo(e: Element): Unit = {
    e.demo();
  }
}