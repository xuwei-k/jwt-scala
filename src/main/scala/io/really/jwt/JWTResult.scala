/**
 * Copyright (C) 2014-2015 Really Inc. <http://really.io>
 */
package io.really.jwt

import play.api.libs.json._

/**
 * Represent result from decode operation
 */
sealed abstract class JWTResult extends Product with Serializable

object JWTResult {

  /**
   * Represent JWT data
   * @param header is the header for jwt
   * @param payload is the data for jwt
   */
  case class JWT(header: JWTHeader, payload: JsObject) extends JWTResult

  /**
   * Represent Failure Result from decode operation in case JWT has too many segments
   */
  case object TooManySegments extends JWTResult

  /**
   * Represent Failure Result from decode operation in case JWT has not enough segments
   */
  case object NotEnoughSegments extends JWTResult

  /**
   * Represent Failure Result from decode operation in case JWT is empty
   */
  case object EmptyJWT extends JWTResult

  /**
   * Represent Failure Result from decode operation in case JWT has invalid Signature
   */
  case object InvalidSignature extends JWTResult

  /**
   * Represent Failure Result from decode operation in case JWT has invalid header
   */
  case object InvalidHeader extends JWTResult
}
