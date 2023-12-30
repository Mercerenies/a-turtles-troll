
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.feature.HasEnabledStatus

class FeatureBoundRandomEvent(
  val event: RandomEvent,
  val feature: HasEnabledStatus,
) : RandomEvent by event {

  override fun canFire(state: RandomEventState): Boolean =
    feature.isEnabled() && event.canFire(state)

}
