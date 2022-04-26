
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.container.DropFeatureContainerAdaptor
import com.mercerenies.turtletroll.feature.container.DropFeatureContainer
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeFeatureContainer
import com.mercerenies.turtletroll.feature.container.CompositeDropFeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.drop.BlockBreakEventListener

// Wraps up several DropFeatureContainers into a composite
// FeatureContainer which consists of the drop listener and any other
// provided features.
fun blockBreakListenerContainer(dropFeature: DropFeatureContainer): FeatureContainer {

  // Build a listener to handle the drop rules
  val eventListener = BlockBreakEventListener(dropFeature)

  // Now combine that with anything else provided by the drop feature
  // containers.
  val existingFeatures = DropFeatureContainerAdaptor(dropFeature)
  val listenerFeature = object : AbstractFeatureContainer() {
    override val listeners = listOf(eventListener)
  }
  return CompositeFeatureContainer(listOf(listenerFeature) + existingFeatures)

}
