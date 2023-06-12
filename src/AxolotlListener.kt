
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.location.PlayerSelector

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.entity.Axolotl
import org.bukkit.entity.Player
import org.bukkit.Bukkit

import net.kyori.adventure.text.Component

class AxolotlListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val PEACEFUL_DEATH_MESSAGE: Component =
      Component.text("RIP an axolotl, who died in peace")

    private fun murderDeathMessage(player: Player): Component =
      Component.text("RIP an axolotl, who took ")
        .append(player.displayName())
        .append(" with them")

    val DISTANCE_SQUARED_LIMIT = 1024.0 // 32 blocks (squared)

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(AxolotlListener())

  }

  override val name = "axolotl"

  override val description = "When an axolotl dies, the nearest player also dies"

  @EventHandler
  fun onEntityDeath(event: EntityDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val entity = event.entity
    if (entity is Axolotl) {
      val nearestPlayer = PlayerSelector.findNearestPlayer(entity.location, DISTANCE_SQUARED_LIMIT)

      if (nearestPlayer == null) {
        Messages.broadcastMessage(PEACEFUL_DEATH_MESSAGE)
      } else {
        Messages.broadcastMessage(murderDeathMessage(nearestPlayer))
        nearestPlayer.damage(9999.0, entity)
      }

    }
  }

}
