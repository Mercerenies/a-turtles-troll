
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

import kotlin.random.Random

class EggDropListener(
  val chance: Double = 1.0,
  val minEggs: Int = 1,
  val maxEggs: Int = 4,
  val mobs: Set<EntityType> = DEFAULT_MOBS,
) : AbstractFeature(), Listener {

  companion object {
    val DEFAULT_MOBS = setOf(
      EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER,
      EntityType.HUSK, EntityType.STRAY, EntityType.ZOMBIFIED_PIGLIN,
      EntityType.PIGLIN,
    )
  }

  override val name = "eggdrop"

  override val description = "Several mobs will drop eggs when killed"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.getEntity()
    if (mobs.contains(entity.getType())) {
      if (Random.nextDouble() < chance) {
        event.getDrops().add(ItemStack(Material.EGG, 1))
      }
    }
  }

}
