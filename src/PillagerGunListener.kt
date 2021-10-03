
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.world.ChunkPopulateEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pillager
import org.bukkit.World
import org.bukkit.enchantments.Enchantment

import kotlin.random.Random

class PillagerGunListener(val chance: Double = 1.0) : AbstractFeature(), Listener {

  override val name: String = "ak47"

  override val description: String = "Pillagers spawn with guns instead of crossbows"

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    val entity = event.entity
    if (entity is Pillager) {
      val equipment = entity.getEquipment()!!
      val mainHand = equipment.getItemInMainHand()
      val meta = mainHand.getItemMeta()!!
      meta.setDisplayName("AK47")
      meta.addEnchant(Enchantment.QUICK_CHARGE, 5, true)
      mainHand.setItemMeta(meta)
      equipment.setItemInMainHand(mainHand)
    }
  }

}
