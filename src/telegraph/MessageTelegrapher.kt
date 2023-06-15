
package com.mercerenies.turtletroll.telegraph

import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

// A Telegrapher which sends a message
class MessageTelegrapher(
  val messageSupplier: (Player) -> Component,
) : Telegrapher() {

  constructor(message: Component) : this({ _ -> message })

  constructor(message: String) : this(Component.text(message))

  protected override fun onPerform(player: Player) {
    val message = messageSupplier(player)
    Messages.sendMessage(player, message)
  }

}
