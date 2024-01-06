
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.util.component.*
import com.mercerenies.turtletroll.util.durationOfTicks

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import net.kyori.adventure.title.Title
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object Messages {

  val TITLE_FADE_IN_TIME = 10L
  val TITLE_STAY_TIME = 70L
  val TITLE_FADE_OUT_TIME = 20L

  val TITLE_PARAMS = Title.Times.times(
    durationOfTicks(TITLE_FADE_IN_TIME),
    durationOfTicks(TITLE_STAY_TIME),
    durationOfTicks(TITLE_FADE_OUT_TIME),
  )

  private val prefix: Component =
    Component.text()
      .append("[")
      .append(Component.text("Turtle", NamedTextColor.YELLOW))
      .append("] ")
      .build()

  fun broadcastMessage(message: Component) {
    Bukkit.getServer().broadcast(prefix.append(message))
  }

  fun broadcastMessage(message: String) {
    broadcastMessage(Component.text(message))
  }

  fun sendMessage(sender: CommandSender, message: Component) {
    sender.sendMessage(prefix.append(message))
  }

  fun sendMessage(sender: CommandSender, message: String) {
    sendMessage(sender, Component.text(message))
  }

  fun sendTitle(sender: Player, title: Component, subtitle: Component = Component.text("")) {
    sender.showTitle(Title.title(title, subtitle, TITLE_PARAMS))
  }

  fun broadcastTitle(title: Component, subtitle: Component = Component.text("")) {
    for (player in Bukkit.getOnlinePlayers()) {
      sendTitle(player, title, subtitle)
    }
  }

}
