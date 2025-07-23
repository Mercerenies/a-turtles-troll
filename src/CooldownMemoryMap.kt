
package com.mercerenies.turtletroll

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

import kotlin.collections.HashSet

class CooldownMemoryMap<K, V>(
  val plugin: Plugin,
) {
  private val memory = HashMap<K, V>()

  private inner class CooldownRunnable(val key: K) : BukkitRunnable() {
    override fun run() {
      memory.remove(key)
    }
  }

  fun contains(value: K): Boolean =
    memory.contains(value)

  fun get(key: K): V? =
    memory[key]

  fun add(key: K, value: V, time: Long) {
    memory[key] = value
    CooldownRunnable(key).runTaskLater(plugin, time)
  }

  val entries: Set<Map.Entry<K, V>>
    get() = memory.entries

  val keys: Set<K>
    get() = memory.keys

}
