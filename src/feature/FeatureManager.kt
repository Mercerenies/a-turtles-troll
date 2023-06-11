
package com.mercerenies.turtletroll.feature

import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.command.UnaryCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

import kotlin.collections.joinToString

class FeatureManager(val features: List<Feature>) {

  constructor(_features: Iterable<Feature>) :
    this(_features.toList()) {}

  fun findFeature(name: String): Feature? =
    features.find { it.name.equals(name, ignoreCase = true) }

  val OnCommand = object : UnaryCommand() {

    override fun onCommand(
      sender: CommandSender,
      arg: String,
    ): Boolean {
      val feature = findFeature(arg)
      if (feature == null) {
        return false
      }
      feature.enable()
      Messages.broadcastMessage("${feature.coloredName} is now enabled.")
      return true
    }

    override fun onTabComplete(
      sender: CommandSender,
      arg: String,
    ): List<String>? =
      features.map { it.name }.filter { it.startsWith(arg) }

  }

  val OffCommand = object : UnaryCommand() {

    override fun onCommand(
      sender: CommandSender,
      arg: String,
    ): Boolean {
      val feature = findFeature(arg)
      if (feature == null) {
        return false
      }
      Messages.broadcastMessage("${feature.coloredName} is now disabled.")
      feature.disable()
      return true
    }

    override fun onTabComplete(
      sender: CommandSender,
      arg: String,
    ): List<String>? =
      features.map { it.name }.filter { it.startsWith(arg) }

  }

  val ListCommand = object : TerminalCommand() {

    override fun onCommand(
      sender: CommandSender,
    ): Boolean {
      val msg = features.sortedBy { it.name }.map { it.coloredName }.joinToString(", ")
      sender.sendMessage("[Turtle] ${msg}")
      return true
    }

  }

  val DescribeCommand = object : UnaryCommand() {

    override fun onCommand(
      sender: CommandSender,
      arg: String,
    ): Boolean {
      val feature = findFeature(arg)
      if (feature == null) {
        return false
      }
      sender.sendMessage("[Turtle] ${feature.coloredName} - ${feature.description}")
      return true
    }

    override fun onTabComplete(
      sender: CommandSender,
      arg: String,
    ): List<String>? =
      features.map { it.name }.filter { it.startsWith(arg) }

  }

  val commandMap = mapOf(
    "on" to OnCommand,
    "off" to OffCommand,
    "list" to ListCommand,
  )

}
