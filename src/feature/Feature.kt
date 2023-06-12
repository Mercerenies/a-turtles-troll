
package com.mercerenies.turtletroll.feature

import com.mercerenies.turtletroll.color.ColorCode

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

// Features should initialize in the "enabled" state and may assume
// they are enabled until told otherwise.
interface Feature : HasEnabledStatus {

  val name: String

  val description: String

  fun enable()

  fun disable()

  val coloredName: Component
    get() {
      val color =
        if (isEnabled()) { NamedTextColor.DARK_GREEN } else { NamedTextColor.DARK_RED }
      return Component.text(name, color)
    }

}
