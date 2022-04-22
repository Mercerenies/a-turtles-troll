
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Phantom
import org.bukkit.entity.Witch
import org.bukkit.entity.EntityType
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

import kotlin.collections.HashMap
import kotlin.random.Random

class WitchSummonManager(
  plugin: Plugin,
  val transformChance: Double = 0.05,
) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val CHANCE_INTERVAL = Constants.TICKS_PER_SECOND * 30L

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(WitchSummonManager(state.plugin))

  }

  override val name = "witches"

  override val description = "Phantoms will randomly turn into witches"

  override val taskPeriod = CHANCE_INTERVAL

  override fun run() {
    if (!isEnabled()) {
      return
    }

    for (world in Bukkit.getWorlds()) {
      for (phantom in world.getEntitiesByClass(Phantom::class.java)) {
        if (Random.nextDouble() < transformChance) {
          val location = phantom.location
          // If we kill the phantom, it counts as the player killing
          // the phantom and they get added to
          // PetPhantomManager.safePlayers. We obviously don't want to
          // give the player a freebie, so instead let's just move the
          // phantom out of range.
          phantom.location.x += 9999
          world.spawnEntity(location, EntityType.WITCH)
        }
      }
    }

  }

  @EventHandler
  fun onEntityDamage(event: EntityDamageEvent) {
    if (!isEnabled()) {
      return
    }

    val entity = event.getEntity()

    // Protect witches from falling
    if (entity is Witch) {
      if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
        event.setCancelled(true)
      }
    }

  }

}
