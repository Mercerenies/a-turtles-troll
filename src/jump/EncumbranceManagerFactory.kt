
package com.mercerenies.turtletroll.jump

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.AbstractFeatureContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions
import com.mercerenies.turtletroll.demand.GodsConditionAccessor

import org.bukkit.potion.PotionEffectType
import org.bukkit.Bukkit

abstract class EncumbranceManagerFactory() : FeatureContainerFactory<FeatureContainer> {

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
        InventoryCountStat(state.config.getDouble("encumbrance.penalties.occupied_inventory_slot")),
        ArmorCountStat.Leather(state.config.getDouble("encumbrance.penalties.leather_armor")),
        ArmorCountStat.NonLeather(state.config.getDouble("encumbrance.penalties.non_leather_armor")),
        StatusEffectStat("Slowness", PotionEffectType.SLOWNESS, state.config.getDouble("encumbrance.penalties.slowness")),
        StatusEffectStat("Nausea", PotionEffectType.NAUSEA, state.config.getDouble("encumbrance.penalties.confusion")),
        StatusEffectStat("Jump Boost", PotionEffectType.JUMP_BOOST, state.config.getDouble("encumbrance.penalties.jump_boost")),
        GodsRageStat(state.config.getDouble("encumbrance.penalties.gods_rage"), godsFeature),
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
        "encumbrance" to command.withPermission(Permissions.ENCUMBRANCE),
      )

  }

  abstract fun getCalculator(state: BuilderState): EncumbranceCalculator

  override fun create(state: BuilderState): FeatureContainer =
    Container(EncumbranceManager(getCalculator(state)))

}
