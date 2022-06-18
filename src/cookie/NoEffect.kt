
package com.mercerenies.turtletroll.cookie

object NoEffect : CookieEffect {

  override fun cancelsDefault(): Boolean = false

  override fun onEat(action: CookieEatenAction) {
    action.player.sendMessage("That cookie tastes pretty good.")
  }

}
