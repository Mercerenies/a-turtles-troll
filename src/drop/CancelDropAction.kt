
package com.mercerenies.turtletroll.drop

import org.bukkit.Material

fun CancelDropAction(positivity: Positivity) =
  CancelAndReplaceAction(Material.AIR, positivity)
