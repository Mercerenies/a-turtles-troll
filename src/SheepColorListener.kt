
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*

import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDropItemEvent

class SheepColorListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SheepColorListener())

  }

  override val name = "sheepcolor"

  override val description = "When you shear a sheep, the color of wool that drops is randomized"

  @EventHandler
  fun onEntityDropItem(event: EntityDropItemEvent) {
    if (!isEnabled()) {
      return
    }
    if (event.entity is Sheep) {
      val item = event.itemDrop
      val count = item.itemStack.amount
      item.itemStack = ItemStack(BlockTypes.WOOLS.sample()!!, count)
    }
  }

}
