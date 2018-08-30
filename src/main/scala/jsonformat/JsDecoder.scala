package jsonformat

import scalaz._, Scalaz._
import simulacrum._

@typeclass trait JsDecoder[A] {
  def fromJson(json: JsValue): String \/ A
}

object JsDecoder {
  def fail[A](expected: String, got: JsValue): -\/[String] =
    -\/(s"expected $expected, got $got")

  implicit val long: JsDecoder[Long] = {
    case JsInteger(n) => n.right
    case other        => fail("JsInteger", other)
  }
  implicit val double: JsDecoder[Double] = {
    case JsDouble(n)  => n.right
    case JsInteger(n) => n.toDouble.right // potential loss of precision
    case other        => fail("JsDouble or JsInteger", other)
  }
  implicit val boolean: JsDecoder[Boolean] = {
    case JsBoolean(x) => x.right
    case other        => fail("JsBoolean", other)
  }
  implicit val string: JsDecoder[String] = {
    case JsString(x) => x.right
    case other       => fail("JsString", other)
  }
}
