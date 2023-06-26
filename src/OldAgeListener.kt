
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.gravestone.CustomDeathMessageRegistry
import com.mercerenies.turtletroll.gravestone.CustomDeathMessage
import com.mercerenies.turtletroll.gravestone.OldAge
import com.mercerenies.turtletroll.feature.AbstractFeature

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLevelChangeEvent

import net.kyori.adventure.text.Component

class OldAgeListener(
  private val deathRegistry: CustomDeathMessageRegistry,
  val ageLimit: Int = 100,
) : AbstractFeature(), Listener {

  companion object {
    // This is public because LearningFromFailureListener needs to
    // know where we get our config from, so that it can prevent
    // infinite loops between itself and this listener.
    val AGE_LIMIT_CONFIG_PATH: String = "oldage.age_limit"
  }

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
        Component.text("").append(event.player.displayName()).append(" died of old age."),
      )
      deathRegistry.withCustomDeathMessage(message) {
        event.player.damage(9999.0, null)
      }
    }
  }

}
