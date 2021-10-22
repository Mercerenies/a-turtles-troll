
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PumpkinSlownessManager(val plugin: Plugin) : RunnableFeature(), Listener {

  companion object {
    val TICKS_PER_SECOND = 20
  }

  override val name = "pumpkins"

  override val description = "Pumpkins on your head cause negative status effects but let you swim"

  fun performPumpkinCheck() {
    if (!isEnabled()) {
      return
    }
    for (player in Bukkit.getOnlinePlayers()) {
      if (player.inventory.helmet?.getType() == Material.CARVED_PUMPKIN) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, TICKS_PER_SECOND * 10, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, TICKS_PER_SECOND * 10, 0))
      }
    }
  }

  override fun run() {
    performPumpkinCheck()
  }

  @EventHandler
  fun onInventoryClose(@Suppress("UNUSED_PARAMETER") event: InventoryCloseEvent) {
    performPumpkinCheck()
  }

  fun register() {
    this.runTaskTimer(plugin, 1L, TICKS_PER_SECOND * 5L)
  }

}
