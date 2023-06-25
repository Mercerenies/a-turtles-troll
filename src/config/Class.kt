
package com.mercerenies.turtletroll.config

import org.bukkit.configuration.ConfigurationSection

interface Class<T> {

  object INT : Class<Int> {
    override fun fromBukkitConfig(config: ConfigurationSection, path: String): Int? =
      if (config.isInt(path)) {
        config.getInt(path)
      } else {
        null
      }
  }

  object STRING : Class<String> {
    override fun fromBukkitConfig(config: ConfigurationSection, path: String): String? =
      if (config.isString(path)) {
        config.getString(path)!!
      } else {
        null
      }
  }

  fun fromBukkitConfig(config: ConfigurationSection, path: String): T?

}
