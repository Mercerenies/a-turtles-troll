
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Zombie

import kotlin.random.Random

class NamedZombieListener(
  val chance: Double = 0.20,
  val nameSource: NameSource = DEFAULT_NAME_SOURCE,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val DEFAULT_NAME_SOURCE = NameSource.FromList(
      "Mercerenies", "jbax1899", "Evanski_", "RekNepZ_HBK", "HatCrafter",
      "Lukasmah", "niko474",
    )
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(NamedZombieListener())

  }

  override val name = "namedzombie"

  override val description = "Some zombies spawn with names"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Zombie) {
      if (Random.nextDouble() < chance) {
        entity.customName(nameSource.sampleName())
        entity.setCustomNameVisible(true)
      }
    }
  }

}
