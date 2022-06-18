
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object DirtDropFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  val FREQUENT_DIRT_DROP_TRIGGERS = setOf(
    Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
    Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
    Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS,
    Material.COPPER_ORE, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE,
    Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE,
    Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE,
  )

  val REGULAR_DIRT_DROP = ReplaceDropsAction(ItemStack(Material.DIRT, 64))
  val FREQUENT_DIRT_DROP = REGULAR_DIRT_DROP.filter {
    FREQUENT_DIRT_DROP_TRIGGERS.contains(it.block.type)
  }

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val regularDirtDrop = REGULAR_DIRT_DROP.asFeature("dirt1", "")
    private val frequentDirtDrop = FREQUENT_DIRT_DROP.asFeature("dirt2", "")
    private val dirtDropFeature = CompositeFeature(
      "dirtstacks",
      "Replaces drops with dirt stacks at random",
      listOf(regularDirtDrop, frequentDirtDrop),
    )

    override val features = listOf(dirtDropFeature)

    override val actions = listOf(Weight(regularDirtDrop, 0.3), Weight(frequentDirtDrop, 1.0))

  }

}
