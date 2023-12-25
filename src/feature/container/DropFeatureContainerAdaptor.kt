
package com.mercerenies.turtletroll.feature.container

// Adapts a DropFeatureContainer to be a FeatureContainer.
class DropFeatureContainerAdaptor(
  private val impl: DropFeatureContainer,
) : FeatureContainer, BaseFeatureContainer by impl
