
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.ext.*

import net.kyori.adventure.text.Component

interface EncumbranceContribution {

  companion object {

    operator fun invoke(amount: Double, reason: Component): EncumbranceContribution =
      Simple(amount, reason)

    operator fun invoke(amount: Double, reason: String): EncumbranceContribution =
      invoke(amount, Component.text(reason))

  }

  private class Simple(
    override val amount: Double,
    val reason: Component,
  ) : EncumbranceContribution {

    override val explanation: Component
      get() {
        val percent = EncumbranceCalculator.formatPercent(amount)
        return Component.text("* ").append(percent).append(" ").append(reason)
      }

  }

  // This function shall return a number from 0 to 1, indicating the
  // percentage chance that a jump shall fail.
  val amount: Double

  val explanation: Component

}
