
package com.mercerenies.turtletroll.config

import org.bukkit.configuration.ConfigurationSection

// Configuration backed by a Bukkit configuration object
class BukkitConfigOptions(
  private val config: ConfigurationSection,
) : ConfigOptions {

  override fun<T> getValue(classType: Class<T>, path: String): T? =
    classType.fromBukkitConfig(config, path)

}
