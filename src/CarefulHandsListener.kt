
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.util.withEnchantment

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

import kotlin.random.Random

class CarefulHandsListener(
  private val chance: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        CarefulHandsListener(
          chance = state.config.getDouble("carefulhands.probability"),
        )
      )

    private val silkTouchTool: ItemStack =
      ItemStack(Material.IRON_PICKAXE).withEnchantment(Enchantment.SILK_TOUCH, 1)

  }

  override val name = "carefulhands"

  override val description = "Every time a player breaks a block, there's a small chance the game will treat it as though they used silk touch"

  @EventHandler(priority = EventPriority.LOW)
  fun onBlockBreak(event: BlockBreakEvent) {
    if (!isEnabled()) {
      return
    }
    if (Random.nextDouble() < chance) {
      event.setCancelled(true)
      event.block.breakNaturally(silkTouchTool)
    }
  }

}
