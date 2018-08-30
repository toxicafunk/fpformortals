package http.oauth2.client.api

import eu.timepit._
import refined.api.Refined
import refined.string.Url

final case class AuthRequest(
  redirect_uri: String Refined Url,
  scope: String,
  client_id: String,
  prompt: String = "consent",
  response_type: String = "code",
  access_type: String = "offline"
)

final case class AccessRequest(
  code: String,
  redirect_uri: String Refined Url,
  client_id: String,
  client_secret: String,
  scope: String = "",
  grant_type: String = "authorization_code"
)

final case class AccessResponse(
  access_token: String,
  token_type: String,
  expires_in: Long,
  refresh_token: String
)

import jsonformat._, JsDecoder.ops._

object AccessResponse {
  implicit val json: JsDecoder[AccessResponse] = j =>
  for {
    acc <- j.getAs[String]("access_token")
    tpe <- j.getAs[String]("token_type")
    exp <- j.getAs[Long]("expires_in")
    ref <- j.getAs[String]("refresh_token")
  } yield AccessResponse(acc, tpe, exp, ref)
}

final case class RefreshRequest(
  client_secret: String,
  refresh_token: String,
  client_id: String,
  grant_type: String = "refresh_token"
)

final case class RefreshResponse(
  access_token: String,
  token_type: String,
  expires_in: Long
)

object RefreshResponse {
  implicit val json: JsDecoder[RefreshResponse] = j =>
  for {
    acc <- j.getAs[String]("access_token")
    tpe <- j.getAs[String]("token_type")
    exp <- j.getAs[Long]("expires_in")
  } yield RefreshResponse(acc, tpe, exp)
}
