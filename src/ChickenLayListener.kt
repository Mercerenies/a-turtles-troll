
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.inventory.ItemStack
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockSpreadEvent
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.plugin.Plugin
import org.bukkit.entity.Chicken
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.block.Block
import org.bukkit.block.`data`.`type`.Bamboo

import kotlin.random.Random

class ChickenLayListener(
    val chanceOfReplacing: Double,
) : AbstractFeature(), Listener {
  companion object : FeatureContainerFactory<FeatureContainer> {
    private val REPLACEMENT_OPTIONS = listOf(
        Material.COAL, Material.DIAMOND, Material.EMERALD, Material.NETHER_QUARTZ_ORE,
        Material.IRON_NUGGET, Material.GOLD_NUGGET, Material.COPPER_INGOT, Material.REDSTONE,
        Material.LAPIS_LAZULI,
    )

    override fun create(state: BuilderState): FeatureContainer {
      val chance = state.config.getDouble("chickenlay.chance")
      return ListenerContainer(ChickenLayListener(chance))
    }
  }

  override val name = "chickenlay"

  override val description = "Chickens can lay various types of ores"

  @EventHandler
  fun onEntityDropItem(event: EntityDropItemEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.entity !is Chicken) {
      return
    }
    if (Random.nextDouble() < chanceOfReplacing) {
      event.setCancelled(true)
      val item = event.entity.world.spawn(event.entity.location, Item::class.java)
      item.itemStack = ItemStack(REPLACEMENT_OPTIONS.random(), 1)
      event.entity.world.playSound(event.entity.location, Sound.ENTITY_CHICKEN_EGG, 0.5f, 0.5f)
    }
  }

}
