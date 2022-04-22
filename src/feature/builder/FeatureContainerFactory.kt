
package com.mercerenies.turtletroll.feature.builder

import com.mercerenies.turtletroll.storage.GlobalDataHolder
import com.mercerenies.turtletroll.feature.container.CompositeFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer

fun interface FeatureContainerFactory<out T : FeatureContainer> {

  companion object {

    fun createComposite(factories: Iterable<FeatureContainerFactory<FeatureContainer>>, builderState: BuilderState): FeatureContainer {
      // This could be an Iterable.map call, but since the factories
      // will often carry side effects, I want to make it very
      // explicit the order in which these are happening.
      val containers = ArrayList<FeatureContainer>()
      for (factory in factories) {
        containers.add(factory.create(builderState))
      }
      return CompositeFeatureContainer(containers)
    }

  }

  fun create(state: BuilderState): T

}
