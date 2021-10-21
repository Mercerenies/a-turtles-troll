
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.entity.Player
import org.bukkit.entity.Ghast
import org.bukkit.plugin.Plugin
import org.bukkit.Location
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block

class GhastBurnRunnable(val plugin: Plugin) : RunnableFeature() {

  companion object {
    val TICKS_PER_SECOND = 20L

    private fun getOverworld(): World? =
      Bukkit.getServer().getWorlds().find { it.environment == World.Environment.NORMAL }

  }

  override val name: String = "ghastburn"

  override val description: String = "Ghasts burn in daylight"

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val overworld = getOverworld()
    if (overworld != null) {
      overworld.getEntitiesByClass(Ghast::class.java).forEach { ghast ->
        if (ghast.location.block.getLightFromSky() >= 15) {
          // Can't set them on fire so we'll just insta-kill them
          ghast.damage(9999.0, null)
        }
      }
    }
  }

  fun register() {
    this.runTaskTimer(plugin, 1L, TICKS_PER_SECOND * 10L)
  }

}
