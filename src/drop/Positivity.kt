
package com.mercerenies.turtletroll.drop

// Whether a BlockBreakAction is beneficial to the player (POSITIVE),
// harmful to the player (NEGATIVE), or ambivalent (NEUTRAL). POSITIVE
// events fire more often when the player has Luck and NEGATIVE ones
// fire more often when the player has Unluck.
enum class Positivity {
  NEGATIVE,
  NEUTRAL,
  POSITIVE,
}
