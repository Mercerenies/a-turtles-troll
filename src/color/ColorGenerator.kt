
package com.mercerenies.turtletroll.color

import org.bukkit.Color

import kotlin.random.Random

fun interface ColorGenerator {

  fun generate(random: Random): Color

}
