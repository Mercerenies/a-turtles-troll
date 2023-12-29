
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.ForestFireListener
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material

object BedrockFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  val BEDROCK_BLOCKS =
    ForestFireListener.BLOCKS + setOf(
      Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK,
      Material.DIORITE, Material.ANDESITE, Material.GRANITE,
      Material.BASALT, Material.BLACKSTONE,
    )

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val bedrockAction = BedrockAction.filter {
      BEDROCK_BLOCKS.contains(it.block.type)
    }.asFeature(
      "bedrock",
      "Several block types transform into bedrock when mined",
    )

    override val features = listOf(bedrockAction)

    override val preRules = listOf(bedrockAction)

  }

}
