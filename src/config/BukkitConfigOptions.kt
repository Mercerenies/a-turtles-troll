
package com.mercerenies.turtletroll.config

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration

import java.io.InputStreamReader

// Configuration backed by a Bukkit configuration object
class BukkitConfigOptions(
  private val config: ConfigurationSection,
) : ConfigOptions {

  companion object {

    fun fromCurrentJar(path: String): BukkitConfigOptions =
      BukkitConfigOptions::class.java.getResourceAsStream(path).use { stream ->
        val reader = InputStreamReader(stream)
        BukkitConfigOptions(YamlConfiguration.loadConfiguration(reader))
      }

  }

  override fun<T> getValue(classType: Class<T>, path: String): T? =
    classType.fromBukkitConfig(config, path)

}
