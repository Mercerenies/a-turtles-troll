
package com.mercerenies.turtletroll.feature

abstract class AbstractFeature : Feature {
  private var _enabled: Boolean = true

  open override fun enable() {
    _enabled = true
  }

  open override fun disable() {
    _enabled = false
  }

  override fun isEnabled(): Boolean = _enabled

}
