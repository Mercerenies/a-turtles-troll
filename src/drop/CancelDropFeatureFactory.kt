
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.BedDropListener
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractDropFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material

object CancelDropFeatureFactory : FeatureContainerFactory<DropFeatureContainer> {

  val NO_DROP_ON = setOf(
    Material.CRAFTING_TABLE, Material.FURNACE,
    Material.SMOKER, Material.BLAST_FURNACE,
  )

  override fun create(state: BuilderState): DropFeatureContainer = object : AbstractDropFeatureContainer() {

    private val cancelDropAction = CancelDropAction.filter {
      NO_DROP_ON.contains(it.block.type)
    }.asFeature(
      "nodrops",
      "Several block types refuse to drop when broken",
    )

    private val bedListener = BedDropListener()

    private val dropCompositeFeature =
      CompositeFeature(
        cancelDropAction.name,
        cancelDropAction.description,
        listOf(cancelDropAction, bedListener)
      )

    override val listeners = listOf(bedListener)

    override val features = listOf(dropCompositeFeature)

    override val preRules = listOf(cancelDropAction)

  }

}
