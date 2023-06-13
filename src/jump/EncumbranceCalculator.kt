
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.ext.*
import com.mercerenies.turtletroll.Messages

import org.bukkit.entity.Player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

interface EncumbranceCalculator {

  companion object {
    fun formatPercent(value: Double): Component =
      Component.text(String.format("%2.2f%%", value * 100), NamedTextColor.GOLD)
  }

  class Sum(val elements: List<EncumbranceStat>) : EncumbranceCalculator {

    constructor(vararg elements: EncumbranceStat) : this(elements.toList())

    override fun calculateEncumbrance(player: Player): Double =
      elements.map { it.calculateEncumbrance(player).amount }.sum()

    override fun explain(player: Player) {
      val total = calculateEncumbrance(player)
      Messages.sendMessage(player, Component.text("Total Encumbrance: ").append(formatPercent(total)))
      for (element in elements) {
        Messages.sendMessage(player, element.calculateEncumbrance(player).explanation)
      }
    }

  }

  // This function shall return a number from 0 to 1, indicating the
  // percentage chance that a jump shall fail.
  fun calculateEncumbrance(player: Player): Double

  fun explain(player: Player): Unit

}
