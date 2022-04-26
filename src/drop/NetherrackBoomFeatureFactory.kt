
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
import com.mercerenies.turtletroll.drop.nearby.NetherrackBoomAction

import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object NetherrackBoomFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val netherrackBoomAction = NetherrackBoomAction().asFeature(
      "netherrack",
      "Common nether materials cause a cascading effect, breaking nearby blocks when broken",
    )

    override val features = listOf(netherrackBoomAction)

    override val preRules = listOf(netherrackBoomAction)

  }

}
