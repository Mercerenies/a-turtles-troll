
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

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

class SandblasterFeature(
  private val plugin: Plugin,
  private val sandRadius: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val feature = SandblasterFeature(
        plugin = state.plugin,
        sandRadius = state.config.getDouble("sandblaster.sand_radius")
      )
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

  }

  private inner class SandblasterEvent() : NotifiedRandomEvent(plugin) {
    override val name = "sandblaster"
    override val baseWeight = 0.6
    override val deltaWeight = 0.2

    override val messages = listOf(
      Component.text("Watch out, it's a sandy blast,"),
      Component.text("A whirlwind of fun, a storm so vast."),
      Component.text("Grab your shades, hold on tight,"),
      Component.text("Sandblaster's here, pure dynamite!"),
    )

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      for (player in Bukkit.getOnlinePlayers()) {
        dropSand(player)
      }
    }

    private fun dropSand(player: Player) {
      player.world.playSound(player.location, Sound.BLOCK_SAND_STEP, 1.0f, 0.0f)
      val center = player.location.clone().add(0.0, 20.0, 0.0)
      val nearbyBlocks = BlockSelector.getSphere(center, sandRadius)
        .filter { it.block.type == Material.AIR }
      for (loc in nearbyBlocks) {
        loc.block.type = Material.SAND
      }
    }
  }

  override val name: String = "sandblaster"

  override val description: String = "Massive blobs of sand will occasionally fall on all players"

  val randomEvent: RandomEvent =
    SandblasterEvent()
      .withTitle("Sandblaster!")
      .withCooldown(12)
      .boundToFeature(this)

}
