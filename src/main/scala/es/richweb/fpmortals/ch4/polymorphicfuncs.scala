package es.richweb.fpmortals.ch4

trait Ordering[T] {
  def compare(x: T, y: T): Int

  def lt(x: T, y: T): Boolean = compare(x, y) < 0
  def gt(x:T, y: T): Boolean = compare(x, y) > 0
}

trait Numeric[T] extends Ordering[T] {
  def plus(x: T, y: T): T
  def times(x: T, y: T): T
  def negate(x: T): T
  def zero: T

  def abs(x: T): T = if (lt(x, zero)) negate(x) else x
}

object Numeric {
  def apply[T](implicit numeric: Numeric[T]): Numeric[T] = numeric
}

object polymorphicfuncs {
  def signOfTheTimes[T](t: T)(implicit N: Numeric[T]): T = {
    import N._
    times(negate(abs(t)), t)
  }

  def signOfTheTimes1[T: Numeric](t: T): T = {
    implicitly[Numeric[T]].times(
      implicitly[Numeric[T]].negate(
        implicitly[Numeric[T]].abs(t)
      ),
      t
    )
  }

  def signOfTheTimes2[T: Numeric](t: T): T =  {
    val N = Numeric[T]
    import N._
    times(negate(abs(t)), t)
  }
}
