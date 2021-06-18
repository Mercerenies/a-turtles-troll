
package com.mercerenies.turtletroll.egg

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.sample
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.util.Vector
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerEggThrowEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

import kotlin.random.Random

class EggListener(val effects: List<Weight<EggHatchEffect>>) : AbstractFeature(), Listener {

  override val name = "eggs"

  override val description = "Throwing eggs can hatch just about anything"

  @EventHandler
  fun onPlayerEggThrow(event: PlayerEggThrowEvent) {
    if (!isEnabled()) {
      return
    }
    event.setHatching(true)
    event.setNumHatches(1)
  }

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if ((event.spawnReason == CreatureSpawnEvent.SpawnReason.EGG) || (event.spawnReason == CreatureSpawnEvent.SpawnReason.DISPENSE_EGG)) {
      event.setCancelled(true)
      sample(effects).onEggHatch(event.location)
    }
  }

}
