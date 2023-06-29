
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.Player
import org.bukkit.enchantments.Enchantment

class BlockBreakEventListener(
  private val preRules: List<BlockBreakAction>,
  private val actions: List<Weight<BlockBreakAction>>,
  private val postRules: List<BlockBreakAction>,
) : Listener {

  companion object {

    fun isUsingSilkTouch(player: Player): Boolean =
      player.inventory.getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)

  }

  // Note: We add in a unit weight NullAction here, since that won't
  // be provided as a "feature" in any sense.
  constructor(dropFeature: DropFeatureContainer) : this(
    dropFeature.preRules.toList(),
    listOf(Weight(NullAction, 1.0)) + dropFeature.actions.toList(),
    dropFeature.postRules.toList(),
  ) {}

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {

    if (isUsingSilkTouch(event.player)) {
      // Silk Touch negates all drop-based events. Allow it to pass
      // unscathed.
      return
    }

    for (action in preRules) {
      if (action.shouldTrigger(event)) {
        action.trigger(event)
        if (action.fullyOverridesOthers()) {
          return
        }
      }
    }

    // Regular dice roll
    val validActions = actions.filter { it.value.shouldTrigger(event) }
    if (!validActions.isEmpty()) {
      val action = sample(validActions)
      action.trigger(event)
      if (action.fullyOverridesOthers()) {
        return
      }
    }

    for (action in postRules) {
      if (action.shouldTrigger(event)) {
        action.trigger(event)
        if (action.fullyOverridesOthers()) {
          return
        }
      }
    }

  }

}
