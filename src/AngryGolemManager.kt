
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.entity.IronGolem
import org.bukkit.entity.EntityType
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AngryGolemManager(
  plugin: Plugin,
) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    val SPIDER_COOLDOWN_TIME: Long = Constants.TICKS_PER_SECOND * 120L

    override fun create(state: BuilderState): FeatureContainer =
      ManagerContainer(AngryGolemManager(state.plugin))

    fun getAllIronGolems(): List<IronGolem> =
      Bukkit.getWorlds().flatMap { it.getEntitiesByClass(IronGolem::class.java) }

  }

  override val name: String = "angrygolems"

  override val description: String = "Iron golems always spawn angry and ride spiders"

  override val taskPeriod: Long = 2L * Constants.TICKS_PER_SECOND.toLong()

  private val spiderMemory = CooldownMemory<IronGolem>(plugin)

  @EventHandler
  fun onEntityTarget(event: EntityTargetEvent) {
    if (!isEnabled()) {
      return
    }

    // An iron golem should never target a spider that is carrying at
    // least one iron golem (not necessarily the *same* golem)
    val entity = event.entity
    val target = event.target
    if (entity.getType() == EntityType.IRON_GOLEM) {
      if ((target != null) && (target.getType() == EntityType.SPIDER)) {
        if (target.passengers.find { it.getType() == EntityType.IRON_GOLEM } != null) {
          event.setCancelled(true)
        }
      }
    }

  }

  override fun run() {
    if (!isEnabled()) {
      return
    }

    val golems = getAllIronGolems()
    for (golem in golems) {
      // Hand-crafted golems do not get angry
      if (golem.isPlayerCreated()) {
        continue
      }

      golem.target = PlayerSelector.findNearestPlayer(golem.location)
      golem.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Constants.TICKS_PER_SECOND * 999, 1))
      golem.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, Constants.TICKS_PER_SECOND * 999, 1))

      // Give the golem a spider to ride on, if needed
      if ((golem.vehicle == null) && (!spiderMemory.contains(golem))) {
        val spider = golem.location.world!!.spawnEntity(golem.location, EntityType.SPIDER)
        spider.addPassenger(golem)
      }

    }

  }

}
