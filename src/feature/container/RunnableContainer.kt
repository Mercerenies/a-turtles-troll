
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.RunnableFeature

class RunnableContainer(
  private val runnable: RunnableFeature,
) : AbstractFeatureContainer() {

  override val features: Iterable<Feature>
    get() = listOf(runnable)

  override val gameModifications: Iterable<RunnableFeature>
    get() = listOf(runnable)

}
