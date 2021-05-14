
package com.mercerenies.turtletroll.feature

// Features should initialize in the "enabled" state and may assume
// they are enabled until told otherwise.
class CompositeFeature(
  override val name: String,
  override val description: String,
  val features: List<Feature>,
) : AbstractFeature() {

  override fun enable() {
    super.enable()
    for (feature in features) {
      feature.enable()
    }
  }

  override fun disable() {
    super.disable()
    for (feature in features) {
      feature.disable()
    }
  }

}
