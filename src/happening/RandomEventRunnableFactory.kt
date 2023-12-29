
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.container.withDebugCommand

// Note: RandomEventRunnable is NOT a feature. It cannot be enabled or
// disabled. It's simply the backing engine for other features which
// fire at random intervals. The individual features (like pufferfish
// and trivia) can be turned on and off.
class RandomEventRunnableFactory() : FeatureContainerFactory<FeatureContainer> {

  private class Container(
    private val runnable: RandomEventRunnable,
  ) : AbstractFeatureContainer() {
    override val runnables =
      listOf(runnable)
  }

  override fun create(state: BuilderState): FeatureContainer {
    val runnable = RandomEventRunnable(state.plugin)
    return Container(runnable).withDebugCommand("event") { _ ->
      runnable.run()
      true
    }
  }

}
