
package com.mercerenies.turtletroll.feature

object FeatureConfig {

  fun featureEnabledPath(feature: Feature): String =
    "${feature.name}.enabled"

}
