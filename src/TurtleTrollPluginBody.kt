package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.storage.GlobalDataHolder
import com.mercerenies.turtletroll.storage.GlobalFileDataHolder
import com.mercerenies.turtletroll.config.BukkitConfigOptions
import com.mercerenies.turtletroll.config.CheckedConfigOptions
import com.mercerenies.turtletroll.config.DifficultyBackedConfigOptions
import com.mercerenies.turtletroll.feature.FeatureManager
import com.mercerenies.turtletroll.feature.builder.SimpleBuilderState
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.command.CommandDispatcher
import com.mercerenies.turtletroll.command.Subcommand
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions

// This class is tightly coupled to TurtleTrollPlugin and contains all
// of the feature data for the various plugin features. This class
// only exists to support server reloads. When the server reloads via
// the command `/reload confirm`, this class will be reconstructed
// completely, whereas the TurtleTrollPlugin object is retained across
// `/reload confirm`.
class TurtleTrollPluginBody(
  private val plugin: TurtleTrollPlugin
) {

  val pluginData = GlobalFileDataHolder(plugin)

  val configOptions: CheckedConfigOptions
    get() {
      val bukkitConfig = BukkitConfigOptions(plugin.getConfig())
      return CheckedConfigOptions(DifficultyBackedConfigOptions(bukkitConfig))
    }

  val mainContainer: FeatureContainer =
    AllFeatureFactories.createComposite(this.createBuilderState())
  val featureManager = FeatureManager(mainContainer.features)
  val turtleCommand = Subcommand(
    "turtle" to Subcommand(
      "on" to featureManager.OnCommand.withPermission(Permissions.FEATURE_TOGGLE),
      "off" to featureManager.OffCommand.withPermission(Permissions.FEATURE_TOGGLE),
      "list" to featureManager.ListCommand.withPermission(Permissions.FEATURE_LIST),
      "describe" to featureManager.DescribeCommand.withPermission(Permissions.FEATURE_DESCRIBE),
      "dbg" to Subcommand(
        mainContainer.debugCommands.toMap().mapValues { it.value.withPermission(Permissions.DEBUG) }
      ).withPermission(Permissions.DEBUG),
      *mainContainer.commands.toList().toTypedArray(),
    ).withPermission(Permissions.BASE_COMMAND),
  )
  val commandDispatcher = CommandDispatcher(turtleCommand)

  fun createBuilderState(): BuilderState =
    SimpleBuilderState(plugin, this.pluginData, this.configOptions)

}
