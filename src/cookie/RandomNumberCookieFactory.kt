
package com.mercerenies.turtletroll.cookie

import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin
import org.bukkit.persistence.PersistentDataType

import net.kyori.adventure.text.Component

import kotlin.random.Random

class RandomNumberCookieFactory(
  val plugin: Plugin,
  val displayName: Component?,
) : CookieFactory {

  companion object {
    val MARKER_KEY = "com.mercerenies.turtletroll.cookie.RandomNumberCookieFactory.MARKER_KEY"
  }

  constructor(plugin: Plugin) : this(plugin, null)
  constructor(plugin: Plugin, displayName: String) : this(plugin, Component.text(displayName))

  val markerKey = NamespacedKey(plugin, MARKER_KEY)

  override operator fun invoke(): ItemStack {
    val cookie = ItemStack(Material.COOKIE, 1)
    val meta = cookie.itemMeta!!
    meta.displayName(displayName)
    meta.persistentDataContainer.set(markerKey, PersistentDataType.LONG, Random.nextLong())
    cookie.itemMeta = meta
    return cookie
  }

}
