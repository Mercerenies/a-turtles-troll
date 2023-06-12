
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.Weight

import org.bukkit.plugin.Plugin

abstract class EncumbranceManagerFactory() : FeatureContainerFactory<FeatureContainer> {

  // EncumbranceManagerFactory is an abstract class that can be
  // parameterized by the desired penalties. The companion object is a
  // concrete subclass with a suitable default list.
  companion object : EncumbranceManagerFactory() {

    val COMMAND_PERMISSION = "com.mercerenies.turtletroll.encumbrance"

    override fun getCalculator(plugin: Plugin): EncumbranceCalculator =
      EncumbranceCalculator.Sum(InventoryCountStat(0.05))

  }

  private class Container(
    private val manager: EncumbranceManager,
  ) : AbstractFeatureContainer() {
    private val command: EncumbranceCommand = EncumbranceCommand(manager)

    override val listeners =
      listOf(manager)

    override val features =
      listOf(manager)

    override val commands =
      listOf(
        "encumbrance" to command.withPermission(COMMAND_PERMISSION),
      )

  }

  abstract fun getCalculator(plugin: Plugin): EncumbranceCalculator

  override fun create(state: BuilderState): FeatureContainer =
    Container(EncumbranceManager(getCalculator(state.plugin)))

}
