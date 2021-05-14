
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.block.BlockBreakEvent

class FeatureAction<out T : BlockBreakAction>(
  override val name: String,
  override val description: String,
  val action: T,
) : AbstractFeature(), BlockBreakAction {

  override fun shouldTrigger(event: BlockBreakEvent): Boolean =
    isEnabled() && action.shouldTrigger(event)

  override fun fullyOverridesOthers(): Boolean =
    action.fullyOverridesOthers()

  override fun trigger(event: BlockBreakEvent) =
    action.trigger(event)

}

fun<T : BlockBreakAction> T.asFeature(name: String, description: String): FeatureAction<T> =
  FeatureAction(name, description, this)
