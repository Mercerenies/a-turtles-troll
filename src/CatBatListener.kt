
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.Plugin
import org.bukkit.Location

import kotlin.random.Random

class CatBatListener(
  val plugin: Plugin,
  val chance: Double = 1.0,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    fun getReplacementType(entityType: EntityType): EntityType? =
      when (entityType) {
        EntityType.CAT -> EntityType.BAT
        EntityType.BAT -> EntityType.CAT
        else -> null
      }

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        CatBatListener(
          plugin = state.plugin,
          chance = state.config.getDouble("catbat.probability"),
        )
      )

  }

  private class SummonEntityRunnable(val target: EntityType, val location: Location) : BukkitRunnable() {
    override fun run() {
      location.world!!.spawnEntity(location, target)
    }
  }

  override val name = "catbat"

  override val description = "Cats transform into bats when killed by the player and vice versa"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.getEntity()
    val replacementType = getReplacementType(entity.getType())
    val damageCause = entity.getLastDamageCause()
    if ((Random.nextDouble() < chance) && (replacementType != null)) {
      if ((damageCause is EntityDamageByEntityEvent) && (damageCause.damager is Player)) {
        SummonEntityRunnable(replacementType, entity.location).runTaskLater(plugin, Constants.TICKS_PER_SECOND.toLong())
      }
    }
  }

}
