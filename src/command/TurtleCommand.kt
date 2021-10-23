
package com.mercerenies.turtletroll.command

import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.command.withPermission

fun TurtleCommand(featureManager: FeatureManager): Command =
  Subcommand(
    "turtle" to Subcommand(
      "on" to featureManager.OnCommand.withPermission("com.mercerenies.turtletroll.feature.toggle"),
      "off" to featureManager.OffCommand.withPermission("com.mercerenies.turtletroll.feature.toggle"),
      "list" to featureManager.ListCommand.withPermission("com.mercerenies.turtletroll.feature.list"),
    ).withPermission("com.mercerenies.turtletroll.command"),
  )
