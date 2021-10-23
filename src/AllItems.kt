
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.ext.*

import org.bukkit.Material

object AllItems {

  private val allMaterials = Material.values().toList()

  private fun sampleUnchecked(): Material =
    allMaterials.sample()!!

  fun sample(): Material {
    while (true) {
      val test = sampleUnchecked()
      @Suppress("DEPRECATION")
      if ((test.isItem()) && (!test.isLegacy())) {
        return test
      }
    }
  }

}
