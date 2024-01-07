
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.container.FeatureContainer

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.Player
import org.bukkit.enchantments.Enchantment
import org.bukkit.attribute.Attribute

class BlockBreakEventListener(
  private val preRules: List<BlockBreakAction>,
  private val actions: List<Weight<BlockBreakAction>>,
  private val postRules: List<BlockBreakAction>,
) : Listener {

  companion object {

    fun isUsingSilkTouch(player: Player): Boolean =
      player.inventory.getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)

    private fun adjustWeight(
      event: Weight<BlockBreakAction>,
      playerLuck: Double,
    ): Weight<BlockBreakAction> =
      when (event.value.positivity) {
        Positivity.NEGATIVE ->
          if (playerLuck > 0) {
            Weight(event.value, 0.0)
          } else if (playerLuck < 0) {
            Weight(event.value, event.weight * (1.0 - 2.0 * playerLuck))
          } else {
            event
          }
        Positivity.POSITIVE ->
          if (playerLuck < 0) {
            Weight(event.value, 0.0)
          } else if (playerLuck > 0) {
            Weight(event.value, event.weight * (1.0 + 2.0 * playerLuck))
          } else {
            event
          }
        Positivity.NEUTRAL ->
          // Never change the probability of these
          event
      }

  }

  // Note: We add in a unit weight NullAction here, since that won't
  // be provided as a "feature" in any sense. (TODO Is this the best
  // place for this? Can we do it as part of the feature container
  // API?)
  constructor(dropFeature: FeatureContainer) : this(
    dropFeature.preRules.toList(),
    listOf(Weight(NullAction, 1.0)) + dropFeature.actions.toList(),
    dropFeature.postRules.toList(),
  ) {}

  @EventHandler
  fun onBlockBreak(event: BlockBreakEvent) {

    if (event.isCancelled()) {
      // If something else already interrupted this event, don't fire.
      return
    }

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
    val validActions = getWeightedActions(event)
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

  private fun getWeightedActions(event: BlockBreakEvent): List<Weight<BlockBreakAction>> {
    val playerLuck = event.player.getAttribute(Attribute.GENERIC_LUCK)?.value ?: 0.0
    return actions
      .filter { it.value.shouldTrigger(event) }
      .map { adjustWeight(it, playerLuck) }
  }

}
