
package com.mercerenies.turtletroll

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import kotlin.collections.HashSet

class CooldownMemory<T>(
  plugin: Plugin,
) : Iterable<T> {
  private val impl: CooldownMemoryMap<T, Unit> =
      CooldownMemoryMap(plugin)

  fun contains(value: T): Boolean =
    impl.contains(value)

  fun add(value: T, time: Long) {
    impl.add(value, Unit, time)
  }

  override fun iterator() = impl.keys.iterator()

}
