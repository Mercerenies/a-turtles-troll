
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFormEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.Material

import kotlin.random.Random

class SolidSwapListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    private val MATERIALS_MAP = mapOf(
      Material.COBBLESTONE to Material.OBSIDIAN,
      Material.OBSIDIAN to Material.COBBLESTONE,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(SolidSwapListener())

  }

  override val name = "solidswap"

  override val description = "The water/lava interactions that create cobblestone and obsidian are swapped"

  @EventHandler
  fun onBlockForm(event: BlockFormEvent) {
    if (!isEnabled()) {
      return
    }
    val material = event.newState.getType()
    val newMaterial = MATERIALS_MAP.get(material)
    if (newMaterial != null) {
      event.newState.setType(newMaterial)
    }
  }

}
