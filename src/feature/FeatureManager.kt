
package com.mercerenies.turtletroll.feature

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.Command

import kotlin.collections.joinToString

class FeatureManager(val features: List<Feature>) : CommandExecutor {

  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<String>,
  ): Boolean {
    if (label != "turtle") {
      return false
    }
    val parsed = FeatureParser.parse(args.toList())
    if (parsed == null) {
      return false
    }

    when (parsed) {
      is FeatureParser.On -> {
        val feature = findFeature(parsed.featureName)
        if (feature != null) {
          feature.enable()
          Bukkit.broadcastMessage("[Turtle] §2${feature.name()}§r is now enabled.")
          return true
        } else {
          return false
        }
      }
      is FeatureParser.Off -> {
        val feature = findFeature(parsed.featureName)
        if (feature != null) {
          feature.disable()
          Bukkit.broadcastMessage("[Turtle] §4${feature.name()}§r is now disabled.")
          return true
        } else {
          return false
        }
      }
      FeatureParser.List -> {
        val msg = features.map { it.coloredName() }.joinToString("§r, ")
        sender.sendMessage("[Turtle] ${msg}")
        return true
      }
    }

  }

  private fun findFeature(name: String): Feature? =
    features.find { it.name().equals(name, ignoreCase = true) }

}
