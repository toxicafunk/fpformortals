package es.richweb.fpmortals.ch4

import simulacrum._

@typeclass trait OrderingSim[T] {
  def compare(x: T, y: T): Int
  @op("<") def lt(x: T, y: T): Boolean = compare(x, y) < 0
  @op(">") def gt(x: T, y: T): Boolean = compare(x, y) > 0
}

@typeclass trait NumericSim[T] extends OrderingSim[T] {
  @op("+") def plus(x: T, y: T): T
  @op("*") def times(x: T, y: T): T
  @op("unary_-") def negate(x: T): T
  def zero: T

  def abs(x: T): T = if (lt(x, zero)) negate(x) else x
}

final case class Complex[T](r: T, i: T)

object polymorphicsim {
  import NumericSim.ops._
  def signOfTheTimes[T: NumericSim](t: T): T = -(t.abs) * t

  implicit val NumericDouble: NumericSim[Double] = new NumericSim[Double] {
    def plus(x: Double, y: Double): Double = x + y
    def times(x: Double, y: Double): Double = x * y
    def negate(x: Double): Double = -x
    def zero: Double = 0.0
    def compare(x: Double, y: Double): Int = java.lang.Double.compare(x, y)

    //optimised
    override def lt(x: Double, y: Double): Boolean = x < y
    override def gt(x: Double, y: Double): Boolean = x > y
    override def abs(x: Double): Double = java.lang.Math.abs(x)
  }

  import java.math.{BigDecimal => BD}

  implicit val NumericBD: NumericSim[BD] = new NumericSim[BD] {
    def plus(x: BD, y: BD): BD = x.add(y)
    def times(x: BD, y: BD): BD = x.multiply(y)
    def negate(x: BD): BD = x.negate
    def zero: BD = BD.ZERO
    def compare(x: BD, y: BD): Int = x.compare(y)
  }
}
