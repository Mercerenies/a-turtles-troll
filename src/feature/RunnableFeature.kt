
package com.mercerenies.turtletroll.feature

import org.bukkit.plugin.Plugin

abstract class RunnableFeature(plugin: Plugin) : TurtleRunnable(plugin), Feature {
  private var _enabled: Boolean = true

  open override fun enable() {
    _enabled = true
  }

  open override fun disable() {
    _enabled = false
  }

  override fun isEnabled(): Boolean = _enabled

}
