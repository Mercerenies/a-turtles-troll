
package com.mercerenies.turtletroll.feature

import org.bukkit.scheduler.BukkitRunnable

abstract class RunnableFeature() : BukkitRunnable(), Feature {
  private var _enabled: Boolean = true

  open override fun enable() {
    _enabled = true
  }

  open override fun disable() {
    _enabled = false
  }

  override fun isEnabled(): Boolean = _enabled

}
