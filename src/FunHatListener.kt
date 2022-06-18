
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
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.EntityType

import kotlin.random.Random

class FunHatListener(
  val chance: Double = 0.1,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val ENTITIES = setOf(
      EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER,
      EntityType.SKELETON, EntityType.STRAY,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(FunHatListener())

  }

  override val name = "funhat"

  override val description = "Zombies and skeletons spawn with fun hats and drop them when killed"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (ENTITIES.contains(entity.type)) {
      if (Random.nextDouble() < chance) {
        entity.equipment?.helmet = Hats.sampleRandomHat()
      }
    }
  }

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (ENTITIES.contains(entity.type)) {
      val hat = entity.equipment?.helmet
      if ((hat != null) && (Hats.isCustomHat(hat))) {
        event.drops.add(hat)
      }
    }
  }

}
