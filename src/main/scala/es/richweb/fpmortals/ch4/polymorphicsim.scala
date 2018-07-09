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

object polymorphicsim {
  import NumericSim.ops._
  def signOfTheTimes[T: NumericSim](t: T): T = -(t.abs) * t
}
