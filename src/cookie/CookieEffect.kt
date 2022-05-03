
package com.mercerenies.turtletroll.cookie

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

interface CookieEffect {

  fun cancelsDefault(): Boolean

  fun onEat(stack: ItemStack, player: Player)

}
