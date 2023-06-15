
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.entity.Slime
import org.bukkit.Material

import kotlin.random.Random

class ExpirationDateListener(
  val chance: Double = 0.1,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val SLIME_SIZE = 2
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ExpirationDateListener())

  }

  override val name = "expirationdate"

  override val description = "When a player consumes bread, there's a small chance of a slime spawning"

  @EventHandler
  fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
    if (!isEnabled()) {
      return
    }

    if (event.item.type == Material.BREAD) {
      if (Random.nextDouble() <= chance) {
        val slime = event.player.world.spawn(event.player.location, Slime::class.java)
        slime.size = SLIME_SIZE
      }
    }

  }

}
