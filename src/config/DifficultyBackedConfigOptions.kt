
package com.mercerenies.turtletroll.config

import org.bukkit.Bukkit

class DifficultyBackedConfigOptions(
  private val config: ConfigOptions,
  private val difficultyTiers: Map<String, DifficultyTier> = DifficultyTier.MAP,
) : ConfigOptions {

  private val difficultySetting: DifficultyTier by lazy {
    val setting = config.getString("global.difficulty")
    if (setting == null) {
      Bukkit.getLogger().warning("No difficulty was set in config, assuming \"normal\"")
      DifficultyTier.NORMAL
    } else {
      val tier = difficultyTiers[setting]
      if (tier == null) {
        Bukkit.getLogger().warning("Unknown difficulty $setting, assuming \"normal\"")
        DifficultyTier.NORMAL
      } else {
        tier
      }
    }
  }

  // If the value is
  // * "default"; then read from the current difficulty file
  // * "easy", "normal", "hard", or "insane"; then read from that particular difficulty file
  // * any other value; then take that value
  // * absent or of the wrong type; use the default value given to us by Bukkit
  override fun<T> getValue(classType: Class<T>, path: String): T? =
    readFromDifficulty(classType, path) ?: config.getValue(classType, path)

  private fun<T> readFromDifficulty(classType: Class<T>, path: String): T? =
    config.getString(path).let { stringValue ->
      when (stringValue) {
        "default" -> difficultySetting.configOptions.getValue(classType, path)
        in difficultyTiers -> difficultyTiers[stringValue]!!.configOptions.getValue(classType, path)
        else -> null
      }
    }

}
