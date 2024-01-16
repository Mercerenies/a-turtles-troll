
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

import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener

import net.kyori.adventure.text.Component

class KameksChaosFeature(
  private val plugin: Plugin,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer {
      val feature = KameksChaosFeature(state.plugin)
      return FeatureBuilder()
        .addFeature(feature)
        .addRandomEvent(feature.randomEvent)
        .build()
    }

  }

  private class KameksChaosEvent(plugin: Plugin) : NotifiedRandomEvent(plugin) {
    override val name = "kamekschaos"
    override val baseWeight = 0.4
    override val deltaWeight = 0.2

    override val messages = listOf(Component.text("<Kamek> What's up? The name's Kamek, and I'm here to shuffle all your valuables!"))

    override fun canFire(state: RandomEventState): Boolean =
      true

    override fun onAfterDelay(state: RandomEventState) {
      for (player in Bukkit.getOnlinePlayers()) {
        shuffleInventory(player)
      }
    }

    private fun shuffleInventory(player: Player) {
      player.world.playSound(player.location, Sound.ENTITY_CAT_BEG_FOR_FOOD, 1.0f, 0.0f)
      // Defensive copy, since Bukkit doesn't document whether this is
      // the actual inventory array or not :(
      val items = (player.inventory.contents as Array<ItemStack?>).copyOf()
      items.shuffle()
      player.inventory.contents = items
    }
  }

  override val name: String = "kamekschaos"

  override val description: String = "Kamek will occasionally shuffle all player items"

  val randomEvent: RandomEvent =
    KameksChaosEvent(plugin)
      .withTitle("Kamek's Chaos!")
      .withCooldown(24)
      .onlyIfPlayersOnline()
      .boundToFeature(this)

}
