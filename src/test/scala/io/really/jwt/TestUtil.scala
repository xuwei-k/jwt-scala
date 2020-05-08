package io.really.jwt

import play.api.libs.json.Json
import play.api.libs.json.Json.JsValueWrapper

object TestUtil {
  implicit def algorithmToJson[A <: Algorithm](a: A): JsValueWrapper =
    Json.toJson[Algorithm](a)
}
