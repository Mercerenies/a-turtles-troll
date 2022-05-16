
package com.mercerenies.turtletroll.feature

import com.mercerenies.turtletroll.color.ColorCode

// Features should initialize in the "enabled" state and may assume
// they are enabled until told otherwise.
interface Feature : HasEnabledStatus {

  val name: String

  val description: String

  fun enable()

  fun disable()

  val coloredName: String
    get() {
      val color =
        if (isEnabled()) { ColorCode.DARK_GREEN } else { ColorCode.DARK_RED }
      return color.of(name)
    }

}
