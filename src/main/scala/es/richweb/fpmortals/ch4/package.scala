package es.richweb.fpmortals

package object ch4 {
  //def sin(x: Double): Double = java.lang.Math.sin(x)
  implicit final class DoubleOps(val x: Double) extends AnyVal{
    def sin: Double = java.lang.Math.sin(x)
  }
}
