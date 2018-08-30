package http.encoding

import simulacrum._
import eu.timepit._
import refined.api.Refined

// URL query key=value pairs, in unencoded form.
final case class UrlQuery(params: List[(String, String)])

@typeclass trait UrlQueryWriter[A] {
  def toUrlQuery(a: A): UrlQuery
}

/*@typeclass trait UrlEncodedWriter[A] {
  def toUrlEncoded(a: A): String Refined UrlEncoded
}

object UrlEncodedWriter {
  implicit val encoded: UrlEncodedWriter[String Refined Url] = identity
}*/
