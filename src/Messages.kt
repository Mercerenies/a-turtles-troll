
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.color.ColorCode

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object Messages {

  private val prefix: Component =
    Component.text()
      .append(Component.text("["))
      .append(Component.text("Turtle", NamedTextColor.YELLOW))
      .append(Component.text("] "))
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

}
