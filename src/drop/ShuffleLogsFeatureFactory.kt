
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.Weight
import com.mercerenies.turtletroll.BlockTypes
import com.mercerenies.turtletroll.BlockIgnorer
import com.mercerenies.turtletroll.feature.HasEnabledStatus
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.drop.nearby.NetherrackBoomAction

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object ShuffleLogsFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    // TODO ShuffleDropsAction for stairs/slabs made of wood as well
    private val shuffleLogsAction = ShuffleDropsAction(BlockTypes.LOGS.toList()).asFeature("", "")
    private val shufflePlanksAction = ShuffleDropsAction(BlockTypes.PLANKS.toList()).asFeature("", "")

    private val shuffleFeature = CompositeFeature(
      "shufflelogs",
      "Wooden drops are shuffled",
      listOf(shuffleLogsAction, shufflePlanksAction),
    )

    override val features = listOf(shuffleFeature)

    override val postRules = listOf(shuffleLogsAction, shufflePlanksAction)

  }

}
