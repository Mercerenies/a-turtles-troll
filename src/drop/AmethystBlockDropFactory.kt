
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.BlockIgnorer
import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object AmethystBlockDropFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val amethystBlockDrop = ReplaceDropsAction(ItemStack(Material.GOLDEN_APPLE, 1)).filter {
      it.block.type == Material.AMETHYST_BLOCK
    }.asFeature(
      "amethyst",
      "Amethyst blocks have a small chance of dropping golden apples when mined",
    )

    override val features = listOf(amethystBlockDrop)

    override val actions = listOf(Weight(amethystBlockDrop, 0.1))

  }

}
