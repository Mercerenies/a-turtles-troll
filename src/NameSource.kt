
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit

fun interface NameSource {
  fun sampleName(): String;

  object OnlinePlayers : NameSource {
    override fun sampleName(): String {
      val player = Bukkit.getOnlinePlayers().toList().sample()
      if (player == null) {
        // No one is online :(
        return "";
      } else {
        return player.getName()
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

    override fun sampleName(): String =
      list.sample()!!

  }

}
