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

  object ops {
    implicit class NumericOps[T](t: T)(implicit N: Numeric[T]) {
      def +(o: T): T = N.plus(t, o)
      def *(o: T): T = N.times(t, o)
      def unary_- = N.negate(t)
      def abs: T = N.abs(t)

      //duplicated from Ordering.ops
      def <(o: T): Boolean = N.lt(t, o)
      def >(o: T): Boolean = N.gt(t, o)
    }
  }
}

object polymorphicfuncs {

  implicit val NumericDouble: Numeric[Double] = new Numeric[Double] {
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

  import Numeric.ops._
  def signOfTheTimes3[T: Numeric](t: T): T = -(t.abs) * t
}
