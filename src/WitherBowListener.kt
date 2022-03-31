
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.WitherSkeleton
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

import kotlin.random.Random

class WitherBowListener(val chance: Double = 0.5) : AbstractFeature(), Listener {

  override val name: String = "witherbow"

  override val description: String = "Wither skeletons have a chance of spawning with a bow"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    if (Random.nextDouble() >= chance) {
      return
    }
    val entity = event.entity
    if (entity is WitherSkeleton) {
      val equipment = entity.equipment
      if (equipment != null) {
        equipment.setItemInMainHand(ItemStack(Material.BOW))
      }
    }
  }

}
