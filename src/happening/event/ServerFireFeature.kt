
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
import com.mercerenies.turtletroll.happening.onlyIfPlayersOnline
import com.mercerenies.turtletroll.location.BlockSelector

import org.bukkit.plugin.Plugin
import org.bukkit.block.Block
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

class ServerFireFeature(
  private val plugin: Plugin,
  private val effectRadius: Double,
  private val effectRolls: Int,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    private val MAX_EFFECT_ROLLS = 500

    override fun create(state: BuilderState): FeatureContainer {
      val feature = ServerFireFeature(
        plugin = state.plugin,
        effectRadius = state.config.getDouble("serverfire.radius"),
        effectRolls = state.config.getInt("serverfire.rolls"),
      )
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

    private fun canStartFire(block: Block): Boolean {
      val below = block.getRelative(0, -1, 0)
      return !block.type.isSolid() && below.type.isSolid()
    }
  }

  private inner class ServerFireEvent() : NotifiedRandomEvent(plugin) {
    override val name = "serverfire"
    override val baseWeight = 0.5
    override val deltaWeight = 0.3

    override val messages = listOf(
      Component.text("@Gay Bowser, the server room is on :fire::bangbang:"),
      Component.text("@Gay Bowser, the server room is on :fire::bangbang:"),
      Component.text("@Gay Bowser, the server room is on :fire::bangbang:"),
    )

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      for (player in Bukkit.getOnlinePlayers()) {
        spawnFire(player)
      }
    }

    private fun spawnFire(player: Player) {
      player.world.playSound(player.location, Sound.ENTITY_GHAST_SHOOT, 1.0f, 0.0f)
      val playerLoc = player.location.block
      var successes = 0
      for (i in 0 until MAX_EFFECT_ROLLS) {
        val targetLoc = BlockSelector.getRandomBlockNearDims(playerLoc, distance = effectRadius.toInt())
        if ((targetLoc.location != playerLoc.location) && (canStartFire(targetLoc))) {
          targetLoc.type = Material.FIRE
          successes += 1
        }
        if (successes > effectRolls) {
          break
        }
      }
    }
  }

  override val name: String = "cataclysm"

  override val description: String = "Lava, magma blocks, and gravel are spawned near all players at random"

  val randomEvent: RandomEvent =
    ServerFireEvent()
      .withTitle("Server Fire!")
      .withCooldown(24)
      .onlyIfPlayersOnline()
      .boundToFeature(this)

}
