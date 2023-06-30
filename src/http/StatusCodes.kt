
package com.mercerenies.turtletroll.http

object StatusCodes {

  fun isSuccessful(code: Int): Boolean =
    code / 100 == 2

}
