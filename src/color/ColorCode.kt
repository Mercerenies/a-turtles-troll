
package com.mercerenies.turtletroll.color

data class ColorCode(val prefix: String) {

  companion object {

    val SIGIL = "§"

    /* ktlint-disable no-multi-spaces */
    val BLACK        = ColorCode("§0")
    val DARK_BLUE    = ColorCode("§1")
    val DARK_GREEN   = ColorCode("§2")
    val DARK_AQUA    = ColorCode("§3")
    val DARK_RED     = ColorCode("§4")
    val DARK_PURPLE  = ColorCode("§5")
    val GOLD         = ColorCode("§6")
    val GRAY         = ColorCode("§7")
    val DARK_GRAY    = ColorCode("§8")
    val BLUE         = ColorCode("§9")
    val GREEN        = ColorCode("§a")
    val AQUA         = ColorCode("§b")
    val RED          = ColorCode("§c")
    val LIGHT_PURPLE = ColorCode("§d")
    val YELLOW       = ColorCode("§e")
    val WHITE        = ColorCode("§f")
    val OBFUSCATED   = ColorCode("§k")
    val BOLD         = ColorCode("§l")
    val STRIKE       = ColorCode("§m")
    val UNDERLINE    = ColorCode("§n")
    val ITALIC       = ColorCode("§o")
    val RESET        = ColorCode("§r")
    /* ktlint-enable no-multi-spaces */

  }

  fun of(text: String) =
    "${prefix}${text}${RESET.prefix}"

}
