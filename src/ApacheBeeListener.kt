
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Bee

import net.kyori.adventure.text.Component

import kotlin.random.Random

class ApacheBeeListener(
  val chance: Double,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ApacheBeeListener(state.config.getDouble("apache.probability")))

  }

  override val name = "apache"

  override val description = "Bees have a chance of spawning named \"Apache\""

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity !is Bee) {
      return
    }
    if (Random.nextDouble() < chance) {
      entity.customName(Component.text("Apache"))
    }
  }

}
