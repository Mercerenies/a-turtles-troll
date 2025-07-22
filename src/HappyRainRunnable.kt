
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Item

import kotlin.random.Random

class HappyRainRunnable(
    plugin: Plugin,
    private val dropProbability: Double,
    private val dropPeriod: Int,
) : RunnableFeature(plugin) {
  companion object : FeatureContainerFactory<FeatureContainer> {
    private val DROP_CANDIDATES = listOf(
        Weight(ItemStack(Material.DIRT, 1), 1.0),
        Weight(ItemStack(Material.DIRT, 64), 0.5),
        Weight(ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1), 1.1),
        Weight(ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 64), 0.8),
        Weight(ItemStack(Material.REDSTONE, 1), 0.7),
        Weight(ItemStack(Material.POINTED_DRIPSTONE, 8), 0.7),
        Weight(ItemStack(Material.POINTED_DRIPSTONE, 16), 0.7),
        Weight(ItemStack(Material.CORNFLOWER, 1), 0.15),
        Weight(ItemStack(Material.DANDELION, 1), 0.15),
        Weight(ItemStack(Material.EGG, 1), 0.09),
        Weight(ItemStack(Material.DIRT, 999), 0.01),
        Weight(ItemStack(Material.DIAMOND, 1), 0.01),
        Weight(ItemStack(Material.EMERALD, 1), 0.01),
        Weight(ItemStack(Material.DIAMOND_BLOCK, 1), 0.001),
    )

    override fun create(state: BuilderState): FeatureContainer {
      val dropProbability = state.config.getDouble("happyrain.drop_probability")
      val dropPeriod = state.config.getInt("happyrain.drop_period")
      val runnable = HappyRainRunnable(
          plugin = state.plugin,
          dropProbability = dropProbability,
          dropPeriod = dropPeriod,
      )
      return RunnableContainer(runnable)
    }
  }

  override val name: String = "happyrain"

  override val description: String = "Every once in awhile, random items fall from the sky"

  override val taskPeriod = Constants.TICKS_PER_SECOND * dropPeriod.toLong()

  override fun run() {
    if (!isEnabled()) {
      return
    }
    if (Random.nextDouble() > dropProbability) {
      return
    }

    val chosenPlayer = Bukkit.getOnlinePlayers().randomOrNull()
    if (chosenPlayer == null) {
      // Nobody is online
      return
    }
    val dropPosition = chosenPlayer.location.clone().add(0.0, 16.0, 0.0)
    val item = chosenPlayer.world.spawn(dropPosition, Item::class.java)
    item.itemStack = sample(DROP_CANDIDATES)
  }
}
