
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.entity.Blaze
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin

class OldAgeListener(val ageLimit: Int = 100) : AbstractFeature(), Listener {

  override val name = "oldage"

  override val description = "Players above a certain level die of old age"

  private var deathTick: Boolean = false // For getting a custom death message

  @EventHandler
  fun onPlayerLevelChange(event: PlayerLevelChangeEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.newLevel >= ageLimit) {
      deathTick = true
      try {
        event.player.damage(9999.0, null)
      } finally {
        deathTick = false
      }
    }
  }

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    if (!isEnabled()) {
      return
    }
    if (deathTick) {
      event.setDeathMessage("${event.entity.getDisplayName()} died of old age.")
    }
  }

}
