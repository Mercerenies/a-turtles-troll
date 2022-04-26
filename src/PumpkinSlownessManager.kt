
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ManagerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PumpkinSlownessManager(plugin: Plugin) : RunnableFeature(plugin), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val PUMPKIN_FEATURE_KEY = "com.mercerenies.turtletroll.PumpkinSlownessManager.PUMPKIN_FEATURE_KEY"

    override fun create(state: BuilderState): FeatureContainer {
      val manager = PumpkinSlownessManager(state.plugin)
      state.registerSharedData(PUMPKIN_FEATURE_KEY, manager)
      return ManagerContainer(manager)
    }

  }

  override val name = "pumpkins"

  override val description = "Pumpkins on your head cause negative status effects but let you swim"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 5L

  fun performPumpkinCheck() {
    if (!isEnabled()) {
      return
    }
    for (player in Bukkit.getOnlinePlayers()) {
      if (player.inventory.helmet?.getType() == Material.CARVED_PUMPKIN) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, Constants.TICKS_PER_SECOND * 10, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, Constants.TICKS_PER_SECOND * 10, 0))
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

}
