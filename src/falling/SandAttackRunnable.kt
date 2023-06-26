
package com.mercerenies.turtletroll.falling

import com.mercerenies.turtletroll.Constants
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.plugin.Plugin

import kotlin.random.Random

class SandAttackRunnable(
  plugin: Plugin,
  val redSandChance: Double = 0.1,
  override val maxDropHeight: Int = 15,
  val targetBlocks: Set<Material> = DEFAULT_TRIGGERS,
) : FallingObjectRunnable(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val DEFAULT_TRIGGERS = setOf(
      Material.SAND, Material.CHISELED_RED_SANDSTONE, Material.CHISELED_SANDSTONE,
      Material.CUT_RED_SANDSTONE, Material.CUT_RED_SANDSTONE_SLAB, Material.CUT_SANDSTONE,
      Material.CUT_SANDSTONE_SLAB, Material.RED_SAND, Material.RED_SANDSTONE,
      Material.RED_SANDSTONE_SLAB, Material.RED_SANDSTONE_STAIRS, Material.RED_SANDSTONE_WALL,
      Material.SANDSTONE, Material.SANDSTONE_SLAB, Material.SANDSTONE_STAIRS,
      Material.SANDSTONE_WALL, Material.SMOOTH_RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE_SLAB,
      Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.SMOOTH_SANDSTONE,
      Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_SANDSTONE_STAIRS,
      Material.GRAVEL, Material.END_STONE,
    )

    override fun create(state: BuilderState): FeatureContainer =
      RunnableContainer(
        SandAttackRunnable(
          plugin = state.plugin,
          redSandChance = state.config.getDouble("sandattack.red_sand_probability"),
          maxDropHeight = state.config.getInt("sandattack.max_drop_height"),
        )
      )

  }

  override val name = "sandattack"

  override val description = "Sand drops on players' heads when standing on certain block types"

  override fun getBlockToDrop() =
    if (Random.nextDouble() < redSandChance) {
      Material.RED_SAND
    } else {
      Material.SAND
    }

  override val taskPeriod = Constants.TICKS_PER_SECOND.toLong()

  override fun shouldDropOn(player: Player): Boolean {
    val blockUnderneath = player.location.clone().add(0.0, -1.0, 0.0).block.type
    return targetBlocks.contains(blockUnderneath) && super.shouldDropOn(player)
  }

  override fun canDropThroughBlock(block: Block): Boolean = true

}
