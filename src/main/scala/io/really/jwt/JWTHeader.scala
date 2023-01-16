package io.really.jwt

import play.api.libs.json._

/**
 * Represent JWT Header
 * @param alg is algorithm that used to encrypt token
 * @param extraHeader is represent
 */
case class JWTHeader(alg: Algorithm, extraHeader: JsObject = Json.obj()) {
  def `type`: String = "JWT"

  def toJson: JsObject = JsObject(
    Seq(
      "typ" -> JsString(`type`),
      "alg" -> Json.toJson(alg)
    ) ++ extraHeader.fields
  )
}
