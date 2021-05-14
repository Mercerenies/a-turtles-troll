
package com.mercerenies.turtletroll.feature

// Features should initialize in the "enabled" state and may assume
// they are enabled until told otherwise.
interface Feature {

  fun name(): String

  fun description(): String

  fun enable()

  fun disable()

  fun isEnabled(): Boolean

  fun coloredName(): String =
    if (isEnabled()) {
      "§2${name()}"
    } else {
      "§4${name()}"
    }

}
