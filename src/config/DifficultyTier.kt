
package com.mercerenies.turtletroll.config

// Configuration backed by a Bukkit configuration object
class DifficultyTier(
  val name: String,
  val filename: String,
) {

  /* ktlint-disable no-multi-spaces comma-spacing paren-spacing */
  companion object {
    val EASY   = DifficultyTier("easy"  , "/config-easy.yml"  )
    val NORMAL = DifficultyTier("normal", "/config-normal.yml")
    val HARD   = DifficultyTier("hard"  , "/config-hard.yml"  )
    val INSANE = DifficultyTier("insane", "/config-insane.yml")

    val ALL_DIFFICULTIES: List<DifficultyTier> = listOf(EASY, NORMAL, HARD, INSANE)

    val MAP: Map<String, DifficultyTier> = ALL_DIFFICULTIES.associateBy { it.name }

  }

  val configOptions: ConfigOptions by lazy {
    BukkitConfigOptions.fromCurrentJar(filename)
  }

}
