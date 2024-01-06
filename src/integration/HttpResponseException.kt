
package com.mercerenies.turtletroll.integration

class HttpResponseException(
  val statusCode: Int,
) : Exception() {

  companion object {

    fun assertHttp2xx(statusCode: Int) {
      if (statusCode < 200 || statusCode >= 300) {
        throw HttpResponseException(statusCode)
      }
    }

  }

  override val message: String =
    "HTTP error: $statusCode"

}
