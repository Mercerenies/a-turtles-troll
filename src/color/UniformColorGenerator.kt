
package com.mercerenies.turtletroll.color

import org.bukkit.Color

import kotlin.random.Random

object UniformColorGenerator : ColorGenerator {

  override fun generate(random: Random): Color {
    val r = random.nextInt(256)
    val g = random.nextInt(256)
    val b = random.nextInt(256)
    return Color.fromRGB(r, g, b)
  }

}
