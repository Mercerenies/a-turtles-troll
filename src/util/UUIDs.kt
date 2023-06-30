
package com.mercerenies.turtletroll.util

import java.util.UUID

object UUIDs {

  fun stringToUuid(string: String): UUID {
    val dashedString =
      if (string.contains("-")) {
        string
      } else {
        // https://stackoverflow.com/a/19399768/2288659
        string.replaceFirst(
          "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
          "$1-$2-$3-$4-$5",
        )
      }
    return UUID.fromString(dashedString)
  }

  fun uuidToString(uuid: UUID, dashes: Boolean): String =
    uuid.toString().let {
      if (dashes) {
        it
      } else {
        it.replace(Regex("-"), "").lowercase()
      }
    }

}
