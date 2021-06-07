
package com.mercerenies.turtletroll

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import kotlin.collections.HashSet

class CooldownMemory<T>(
  val plugin: Plugin,
) {
  private val memory = HashSet<T>()

  private inner class CooldownRunnable(val value: T) : BukkitRunnable() {
    override fun run() {
      memory.remove(value)
    }
  }

  fun contains(value: T): Boolean =
    memory.contains(value)

  fun add(value: T, time: Long) {
    memory.add(value)
    CooldownRunnable(value).runTaskLater(plugin, time)
  }

}
