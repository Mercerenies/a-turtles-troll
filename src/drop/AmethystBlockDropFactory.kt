
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object AmethystBlockDropFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val amethystBlockDrop = ReplaceDropsAction(ItemStack(Material.GOLDEN_APPLE, 1)).filter {
      it.block.type == Material.AMETHYST_BLOCK
    }.asFeature(
      "amethyst",
      "Amethyst blocks have a small chance of dropping golden apples when mined",
    )

    private val probability = state.config.getDouble("amethyst.probability")

    override val features = listOf(amethystBlockDrop)

    override val actions = listOf(Weight(amethystBlockDrop, probability))

  }

}
