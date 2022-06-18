
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.ForestFireListener
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material

object BedrockFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  val BEDROCK_BLOCKS =
    ForestFireListener.BLOCKS + setOf(
      Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK,
      Material.DIORITE, Material.ANDESITE, Material.GRANITE,
      Material.BASALT, Material.BLACKSTONE,
    )

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

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
