package jsonformat

import scalaz._, Scalaz._
import simulacrum._

sealed abstract class JsValue

final case object JsNull                                    extends JsValue
final case class JsObject(fields: IList[(String, JsValue)]) extends JsValue
final case class JsArray(elements: IList[JsValue])          extends JsValue
final case class JsBoolean(value: Boolean)                  extends JsValue
final case class JsString(value: String)                    extends JsValue
final case class JsDouble(value: Double)                    extends JsValue
final case class JsInteger(value: Long)                     extends JsValue

@typeclass trait JsEncoder[A] {
  def toJson(obj: A): JsValue
}

object JsValue {
  implicit class JsValueOps(j: JsValue) {
    def getAs[A: JsDecoder](key: String): String \/ A = JsDecoder[A].fromJson(j)
  }
}
