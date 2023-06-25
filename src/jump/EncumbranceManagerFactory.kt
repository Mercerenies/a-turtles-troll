
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.demand.GodsConditionAccessor

import org.bukkit.potion.PotionEffectType
import org.bukkit.Bukkit

abstract class EncumbranceManagerFactory() : FeatureContainerFactory<FeatureContainer> {

  companion object {
    val COMMAND_PERMISSION = "com.mercerenies.turtletroll.command.encumbrance"
  }

  class Default(
    private val godsFeatureId: String,
  ) : EncumbranceManagerFactory() {

    override fun getCalculator(state: BuilderState): EncumbranceCalculator {
      var godsFeature = state.getSharedData(godsFeatureId, GodsConditionAccessor::class)
      if (godsFeature == null) {
        // Log a warning and use a default value
        Bukkit.getLogger().warning("Could not find gods' status feature, got null")
        godsFeature = GodsConditionAccessor.AlwaysAppeased
      }
      return EncumbranceCalculator.Sum(
        InventoryCountStat(0.0003),
        ArmorCountStat.Leather(0.001),
        ArmorCountStat.NonLeather(0.0025),
        StatusEffectStat("Slowness", PotionEffectType.SLOW, 0.10),
        StatusEffectStat("Nausea", PotionEffectType.CONFUSION, 0.05),
        GodsRageStat(0.01, godsFeature),
      )
    }

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

  abstract fun getCalculator(state: BuilderState): EncumbranceCalculator

  override fun create(state: BuilderState): FeatureContainer =
    Container(EncumbranceManager(getCalculator(state)))

}
