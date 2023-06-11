
package com.mercerenies.turtletroll.cookie

import com.mercerenies.turtletroll.Messages

object NoEffect : CookieEffect {

  override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    Messages.sendMessage(action.player, "That cookie tastes pretty good.")
  }

}
