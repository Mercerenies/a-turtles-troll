
package com.mercerenies.turtletroll.http

import org.bukkit.plugin.Plugin

class PluginUserAgentSupplier(
  private val plugin: Plugin,
) : UserAgentSupplier {

  private val name: String =
    "ATurtlesTroll"

  private val version: String
    get() = plugin.pluginMeta.version

  private val url: String =
    "https://github.com/Mercerenies/a-turtles-troll"

  override fun getUserAgent(): String =
    "$name/$version $url"

}
