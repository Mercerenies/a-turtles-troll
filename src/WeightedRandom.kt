
package com.mercerenies.turtletroll

import kotlin.random.Random

data class Weight<out T>(val value: T, val weight: Double)

// Precondition: List must be nonempty
fun<T> sample(list: List<Weight<T>>, random: Random = Random.Default): T {
  val total = list.map { it.weight }.sum()
  var n = random.nextDouble(total)
  for (curr in list) {
    n -= curr.weight
    if (n < 0) {
      return curr.value
    }
  }
  throw Exception("Internal error in WeightedRandom.sample")
}
