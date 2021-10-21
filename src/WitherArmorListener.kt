
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.WitherSkeleton
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class WitherArmorListener(val chance: Double = 1.0) : AbstractFeature(), Listener {

  override val name: String = "witherskele"

  override val description: String = "Wither skeletons spawn with full diamond armor"

  @EventHandler
  fun onCreatureSpawn(event: CreatureSpawnEvent) {
    if (!isEnabled()) {
      return
    }
    if (!SpawnReason.isNatural(event)) {
      return
    }
    val entity = event.entity
    if (entity is WitherSkeleton) {
      val equipment = entity.equipment
      if (equipment != null) {
        equipment.helmet = ItemStack(Material.DIAMOND_HELMET)
        equipment.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
        equipment.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
        equipment.boots = ItemStack(Material.DIAMOND_BOOTS)
      }
    }
  }

}
