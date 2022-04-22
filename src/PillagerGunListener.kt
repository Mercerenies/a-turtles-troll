
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


class PillagerGunListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer = 
      ListenerContainer(PillagerGunListener())

  }

  override val name: String = "ak47"

  override val description: String = "Pillagers spawn with guns instead of crossbows"

  @EventHandler
  fun onEntitySpawn(event: EntitySpawnEvent) {
    val entity = event.entity
    if (entity is Pillager) {
      val equipment = entity.getEquipment()!!
      val mainHand = equipment.getItemInMainHand()
      val meta = mainHand.getItemMeta()!!
      meta.setDisplayName("AK47")
      meta.addEnchant(Enchantment.QUICK_CHARGE, 5, true)
      mainHand.setItemMeta(meta)
      equipment.setItemInMainHand(mainHand)
    }
  }

}
