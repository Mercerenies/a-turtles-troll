
package com.mercerenies.turtletroll

import org.bukkit.Bukkit

import net.kyori.adventure.text.Component

fun interface NameSource {
  fun sampleName(): Component

  object OnlinePlayers : NameSource {
    override fun sampleName(): Component {
      val player = Bukkit.getOnlinePlayers().toList().randomOrNull()
      if (player == null) {
        // No one is online :(
        return Component.text("")
      } else {
        return player.displayName()
      }
    }
  }

  class FromList(val list: List<String>) : NameSource {

    init {
      if (list.size == 0) {
        throw IllegalArgumentException("Empty list given to NameSource.fromList")
      }
    }

    constructor(vararg args: String) : this(args.toList()) {}

    override fun sampleName(): Component =
      Component.text(list.random())

  }

}
