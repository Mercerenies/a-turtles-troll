
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.Location

import kotlin.random.Random

class CatBatListener(
  val plugin: Plugin,
  val chance: Double = 1.0,
) : AbstractFeature(), Listener {

  companion object {

    fun getReplacementType(entityType: EntityType): EntityType? =
      when (entityType) {
        EntityType.CAT -> EntityType.BAT
        EntityType.BAT -> EntityType.CAT
        else -> null
      }

  }

  private class SummonEntityRunnable(val target: EntityType, val location: Location) : BukkitRunnable() {
    override fun run() {
      location.world!!.spawnEntity(location, target)
    }
  }

  override val name = "catbat"

  override val description = "Cats transform into bats when killed and vice versa"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.getEntity()
    val replacementType = getReplacementType(entity.getType())
    val damageCause = entity.getLastDamageCause()?.getCause() ?: EntityDamageEvent.DamageCause.VOID
    if ((Random.nextDouble() < chance) && (replacementType != null)) {
      // Safeguard so we can still 'kill @a' and not flood the server
      if (damageCause != EntityDamageEvent.DamageCause.VOID) {
        SummonEntityRunnable(replacementType, entity.location).runTaskLater(plugin, Constants.TICKS_PER_SECOND.toLong())
      }
    }
  }

}
