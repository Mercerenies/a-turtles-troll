
package com.mercerenies.turtletroll.feature.container

import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.Weight

// A feature container which also contains drop information.
interface DropFeatureContainer : BaseFeatureContainer {

  val preRules: Iterable<BlockBreakAction>

  val actions: Iterable<Weight<BlockBreakAction>>

  val postRules: Iterable<BlockBreakAction>

}
