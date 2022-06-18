
package com.mercerenies.turtletroll.cookie

interface CookieEffect {

  fun cancelsDefault(): Boolean

  fun onEat(action: CookieEatenAction)

}
