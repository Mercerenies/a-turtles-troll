
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.color.ColorCode

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

object Messages {

  private val prefix: String
    get() = "[" + ColorCode.YELLOW.of("Turtle") + "] "

  fun broadcastMessage(message: String) {
    Bukkit.broadcastMessage(prefix + message)
  }

  fun sendMessage(sender: CommandSender, message: String) {
    sender.sendMessage(prefix + message)
  }

}
