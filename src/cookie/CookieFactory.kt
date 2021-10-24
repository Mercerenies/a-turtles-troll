
package com.mercerenies.turtletroll.cookie

import org.bukkit.inventory.ItemStack

fun interface CookieFactory {

  operator fun invoke(): ItemStack

}
