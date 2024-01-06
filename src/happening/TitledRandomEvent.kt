
package com.mercerenies.turtletroll.happening

import com.mercerenies.turtletroll.Messages

import net.kyori.adventure.text.Component

class TitledRandomEvent(
  val event: RandomEvent,
  val title: Component,
  val subtitle: Component,
) : RandomEvent by event {

  override fun fire(state: RandomEventState) {
    Messages.broadcastTitle(title, subtitle)
    event.fire(state)
  }

}
