
package com.mercerenies.turtletroll.config

import org.bukkit.configuration.ConfigurationSection

interface Class<T> {

  object BOOLEAN : Class<Boolean> {
    override val name = "Boolean"
    override fun fromBukkitConfig(config: ConfigurationSection, path: String): Boolean? =
      if (config.isBoolean(path)) {
        config.getBoolean(path)
      } else {
        null
      }
  }

  object INT : Class<Int> {
    override val name = "Integer"
    override fun fromBukkitConfig(config: ConfigurationSection, path: String): Int? =
      if (config.isInt(path)) {
        config.getInt(path)
      } else if (config.isLong(path)) {
        // Coerce from Long
        config.getLong(path).toInt()
      } else {
        null
      }
  }

  object DOUBLE : Class<Double> {
    override val name = "Double"
    override fun fromBukkitConfig(config: ConfigurationSection, path: String): Double? =
      if (config.isDouble(path)) {
        config.getDouble(path)
      } else {
        // Try to get an integer, if possible
        INT.fromBukkitConfig(config, path)?.toDouble()
      }
  }

  object STRING : Class<String> {
    override val name = "String"
    override fun fromBukkitConfig(config: ConfigurationSection, path: String): String? =
      if (config.isString(path)) {
        config.getString(path)!!
      } else {
        null
      }
  }

  val name: String

  fun fromBukkitConfig(config: ConfigurationSection, path: String): T?

}
