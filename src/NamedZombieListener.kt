
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Zombie

import kotlin.random.Random

class NamedZombieListener(
  val chance: Double = 0.20,
  val nameSource: NameSource = DEFAULT_NAME_SOURCE,
) : AbstractFeature(), Listener {

  companion object {
    val DEFAULT_NAME_SOURCE = NameSource.FromList(
      "Mercerenies", "jbax1899", "Evanski_", "RekNepZ_HBK", "HatCrafter",
    )
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
        entity.setCustomName(nameSource.sampleName())
        entity.setCustomNameVisible(true)
      }
    }
  }

}
