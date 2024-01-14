
package com.mercerenies.turtletroll.happening.event

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.happening.RandomEvent
import com.mercerenies.turtletroll.happening.NotifiedRandomEvent
import com.mercerenies.turtletroll.happening.RandomEventState
import com.mercerenies.turtletroll.happening.withCooldown
import com.mercerenies.turtletroll.happening.withTitle
import com.mercerenies.turtletroll.happening.boundToFeature
import com.mercerenies.turtletroll.location.BlockSelector
import com.mercerenies.turtletroll.BlockTypes
import com.mercerenies.turtletroll.EggshellsListener

import org.bukkit.plugin.Plugin
import org.bukkit.block.Block
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

import kotlin.random.Random

class CataclysmFeature(
  private val plugin: Plugin,
  private val effectRadius: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val REPLACEABLE_BLOCKS: Set<Material> =
      BlockTypes.PLANKS + BlockTypes.LOGS + setOf(
        Material.DIRT, Material.COARSE_DIRT, Material.SAND, Material.RED_SAND,
        Material.NETHERRACK, Material.COBBLESTONE, Material.STONE, Material.BLACKSTONE,
        Material.SOUL_SAND, Material.GRASS_BLOCK,
      )

    private val REPLACEMENT_OPTIONS: Set<Material> =
      setOf(
        Material.MAGMA_BLOCK, Material.GRAVEL, Material.SOUL_SAND,
        Material.SOUL_SOIL, Material.BLACKSTONE, Material.NETHERRACK,
      )

    override fun create(state: BuilderState): FeatureContainer {
      val feature = CataclysmFeature(state.plugin, state.config.getDouble("cataclysm.radius"))
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

    private fun shouldReplaceBlock(block: Block): Boolean =
      REPLACEABLE_BLOCKS.contains(block.type)

    private fun replaceBlock(block: Block) {
      if ((Random.nextDouble() < 0.1) && (EggshellsListener.shouldTurnToLava(block))) {
        // If it's surrounded by solid blocks, there's a chance of
        // turning it to lava.
        block.type = Material.LAVA
      } else {
        block.type = REPLACEMENT_OPTIONS.random()
      }
    }

  }

  private inner class CataclysmEvent() : NotifiedRandomEvent(plugin) {
    override val name = "cataclysm"
    override val baseWeight = 0.1
    override val deltaWeight = 0.1

    override val messages = listOf(
      Component.text("In the heart of chaos, a tumultuous dance"),
      Component.text("Magma rising, gravel's chance"),
      Component.text("Cataclysm strikes with fiery might"),
      Component.text("Hold your ground, embrace the fight!"),
    )

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      for (player in Bukkit.getOnlinePlayers()) {
        spawnMagma(player)
      }
    }

    private fun spawnMagma(player: Player) {
      player.world.playSound(player.location, Sound.BLOCK_LAVA_POP, 1.0f, 0.0f)
      for (loc in BlockSelector.getSphere(player.location, effectRadius)) {
        if (shouldReplaceBlock(loc.block)) {
          replaceBlock(loc.block)
        }
      }
    }
  }

  override val name: String = "cataclysm"

  override val description: String = "Lava, magma blocks, and gravel are spawned near all players at random"

  val randomEvent: RandomEvent =
    CataclysmEvent()
      .withTitle("Cataclysm!")
      .withCooldown(24)
      .boundToFeature(this)

}
