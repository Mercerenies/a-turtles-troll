
package com.mercerenies.turtletroll.durability

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.`data`.Bisected

class ButtonDamageListener(
  override val maxUses: Int = 4,
) : DurabilityListener() {

  companion object : FeatureContainerFactory<FeatureContainer> {

    val BUTTONS = setOf(
      Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON,
      Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.OAK_BUTTON,
      Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON,
      Material.STONE_BUTTON, Material.WARPED_BUTTON, Material.LEVER,
    )

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(ButtonDamageListener())

  }

  override val name = "buttondrop"

  override val description = "Buttons break after some number of uses"

  override fun shouldAffect(block: Block): Boolean =
    BUTTONS.contains(block.type)

}
