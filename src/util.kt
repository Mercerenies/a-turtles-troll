
package com.mercerenies.turtletroll.util

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
