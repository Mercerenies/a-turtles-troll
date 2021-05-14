
package com.mercerenies.turtletroll.feature

// Features should initialize in the "enabled" state and may assume
// they are enabled until told otherwise.
interface Feature {

  val name: String

  val description: String

  fun enable()

  fun disable()

  fun isEnabled(): Boolean

  val coloredName: String
    get() =
      if (isEnabled()) {
        "§2${name}"
      } else {
        "§4${name}"
      }

}
