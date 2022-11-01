
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.plugin.Plugin

class LearningFromFailureListener(
  val ageLimit: Int? = 100,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {
    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(LearningFromFailureListener())
  }

  override val name = "learning"

  override val description = "When a player dies, everyone else gains a level"

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    val dyingPlayer = event.entity
    for (player in Bukkit.getOnlinePlayers()) {
      // Don't upgrade the dying player
      if (player.entityId == dyingPlayer.entityId) {
        continue
      }
      // Don't upgrade players whose level is already at the age limit
      // (to prevent infinite loops with OldAgeListener)
      if ((ageLimit != null) && (player.level >= ageLimit)) {
        continue
      }

      player.sendMessage("You learned an important lesson from ${dyingPlayer.displayName}'s death; +1 level")
      player.level += 1
    }
  }

}
