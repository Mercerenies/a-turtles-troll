
package com.mercerenies.turtletroll.feature

import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin

abstract class RunnableFeature(val plugin: Plugin) : BukkitRunnable(), Feature {
  private var _enabled: Boolean = true

  abstract val taskPeriod: Long

  open val taskDelay: Long = 1L

  open override fun enable() {
    _enabled = true
  }

  open override fun disable() {
    _enabled = false
  }

  override fun isEnabled(): Boolean = _enabled

  fun register() {
    this.runTaskTimer(plugin, taskDelay, taskPeriod)
  }

}
