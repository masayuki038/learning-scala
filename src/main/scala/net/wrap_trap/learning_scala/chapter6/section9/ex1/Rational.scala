package net.wrap_trap.learning_scala.chapter6.section9.ex1

/**
  * Created by masayuki on 2018/02/20.
  */
object Rational {
  def main(args: Array[String]) = {
    val x = new Rational(1, 2)
    val y = new Rational(2, 3)
    println("x + y: " + (x + y))
    println("x.+(y): " + x.+(y))
    println("x + x * y: " + (x + x * y))
    println("(x + x) * y: " + ((x + x) * y))
    println("x + (x * y): " + (x + (x * y)))
  }
}

class Rational(n: Int, d: Int) {

  require(d != 0)

  private val g = gcd(n.abs, d.abs)
  val numer = n / g
  val denom = d / g

  def this(n: Int) = this(n, 1)

  def + (that: Rational): Rational =
    new Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

  def * (that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  override def toString = numer +"/"+ denom

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
}
