
package com.mercerenies.turtletroll.util

import kotlin.math.min
import kotlin.math.max

fun lerp(a: Double, b: Double, amount: Double): Double =
  a * (1 - amount) + b * amount

fun inverseLerp(a: Double, b: Double, value: Double): Double =
  if (a == b) {
    a
  } else {
    (value - a) / (b - a)
  }

fun linearRescale(a0: Double, b0: Double, a1: Double, b1: Double, value: Double): Double =
  lerp(a1, b1, inverseLerp(a0, b0, value))

fun clamp(amount: Double, a: Double, b: Double): Double =
  min(max(amount, a), b)
