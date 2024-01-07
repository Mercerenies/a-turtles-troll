
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object AmethystBlockDropFactory : FeatureContainerFactory<FeatureContainer> {

  override fun create(state: BuilderState): FeatureContainer {
    val amethystBlockDrop = ReplaceDropsAction(ItemStack(Material.GOLDEN_APPLE, 1), Positivity.POSITIVE).filter {
      it.block.type == Material.AMETHYST_BLOCK
    }.asFeature(
      "amethyst",
      "Amethyst blocks have a small chance of dropping golden apples when mined",
    )
    val probability = state.config.getDouble("amethyst.probability")
    return FeatureBuilder()
      .addFeature(amethystBlockDrop)
      .addBreakAction(Weight(amethystBlockDrop, probability))
      .build()
  }

}
