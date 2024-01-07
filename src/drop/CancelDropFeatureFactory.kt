
package com.mercerenies.turtletroll.drop

import com.mercerenies.turtletroll.BedDropListener
import com.mercerenies.turtletroll.feature.CompositeFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material

object CancelDropFeatureFactory : FeatureContainerFactory<FeatureContainer> {

  val NO_DROP_ON = setOf(
    Material.CRAFTING_TABLE, Material.FURNACE,
    Material.SMOKER, Material.BLAST_FURNACE,
  )

  override fun create(state: BuilderState): FeatureContainer = object : AbstractFeatureContainer() {

    private val cancelDropAction = CancelDropAction(Positivity.NEGATIVE).filter {
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
