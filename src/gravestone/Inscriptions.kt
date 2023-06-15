
package com.mercerenies.turtletroll.gravestone

import net.kyori.adventure.text.Component
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.block.sign.SignSide

interface Inscriptions {

  // Receives a number from 0 to 3 inclusive
  fun getLine(index: Int): Component

  fun printTo(signSide: SignSide) {
    for (index in 0..3) {
      signSide.line(index, this.getLine(index))
    }
  }

}
