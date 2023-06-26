
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.entity.Pillager
import org.bukkit.enchantments.Enchantment

import net.kyori.adventure.text.Component

class PillagerGunListener(
  private val quickChargeLevel: Int = 5,
) : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(
        PillagerGunListener(
          quickChargeLevel = state.config.getInt("ak47.quick_charge_level"),
        )
      )

  }

  override val name: String = "ak47"

  override val description: String = "Pillagers spawn with guns instead of crossbows"

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    val entity = event.entity
    if (entity is Pillager) {
      val equipment = entity.getEquipment()
      val mainHand = equipment.getItemInMainHand()
      val meta = mainHand.getItemMeta()!!
      meta.displayName(Component.text("AK47"))
      meta.addEnchant(Enchantment.QUICK_CHARGE, quickChargeLevel, true)
      mainHand.setItemMeta(meta)
      equipment.setItemInMainHand(mainHand)
    }
  }

}
