
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.OldAge
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent

class OldAgeListener(
  private val deathRegistry: CustomDeathMessageRegistry,
  val ageLimit: Int = 100,
) : AbstractFeature(), Listener {

  override val name = "oldage"

  override val description = "Players above a certain level die of old age"

  @EventHandler
  fun onPlayerLevelChange(event: PlayerLevelChangeEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.newLevel >= ageLimit) {
      val message = CustomDeathMessage(
        OldAge,
        "${event.player.getDisplayName()} died of old age.",
      )
      deathRegistry.withCustomDeathMessage(message) {
        event.player.damage(9999.0, null)
      }
    }
  }

}
