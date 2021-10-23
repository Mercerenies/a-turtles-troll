
package com.mercerenies.turtletroll.command

import com.mercerenies.turtletroll.feature.FeatureManager

fun TurtleCommand(featureManager: FeatureManager): Command =
  Subcommand(
    "turtle" to Subcommand(
      "on" to featureManager.OnCommand,
      "off" to featureManager.OffCommand,
      "list" to featureManager.ListCommand,
    ),
  )
