
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Player
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import kotlin.random.Random

class NoYouListener(
  private val odds: Int,
) : AbstractFeature(), Listener {
  companion object : FeatureContainerFactory<FeatureContainer> {
    private val BLACKLISTED_MOBS = listOf(EntityType.WITHER, EntityType.ENDER_DRAGON)

    private fun messageForEntity(entity: Entity): Component =
        Component.text("<")
        .append(entity.name())
        .append("> ")
        .append(Component.text("NO U!", NamedTextColor.DARK_RED))

    override fun create(state: BuilderState): FeatureContainer {
      val odds = state.config.getInt("noyou.odds")
      return ListenerContainer(NoYouListener(odds))
    }

  }

  override val name = "noyou"

  override val description = "When you kill a mob, you might die instead"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val player = event.damageSource.causingEntity
    val dyingEntity = event.entity
    if (player !is Player) {
      return
    }
    if (BLACKLISTED_MOBS.contains(dyingEntity.type)) {
      return // Don't ever trigger this for boss mobs
    }
    if (Random.nextInt(odds) == 0) {
      event.setCancelled(true)
      player.damage(9999.0, dyingEntity)
      Messages.sendMessage(player, messageForEntity(dyingEntity))
    }
  }
}
