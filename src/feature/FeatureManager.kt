
package com.mercerenies.turtletroll.feature

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.util.*
import com.mercerenies.turtletroll.command.TerminalCommand
import com.mercerenies.turtletroll.command.UnaryCommand
import com.mercerenies.turtletroll.Messages

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

import net.kyori.adventure.text.Component

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
      Messages.broadcastMessage(Component.text("").append(feature.coloredName).append(" is now enabled."))
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
      feature.disable()
      Messages.broadcastMessage(Component.text("").append(feature.coloredName).append(" is now disabled."))
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
      val allFeatureNames = features.sortedBy { it.name }.map { it.coloredName }
      Messages.sendMessage(sender, joinWithCommas(allFeatureNames))
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
      Messages.sendMessage(sender, Component.text("").append(feature.coloredName).append(" - ").append(feature.description))
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
