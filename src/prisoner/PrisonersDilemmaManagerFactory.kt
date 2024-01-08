
package com.mercerenies.turtletroll.prisoner

import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.builder.FeatureBuilder
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.command.withPermission
import com.mercerenies.turtletroll.command.Permissions

import org.bukkit.Material

abstract class PrisonersDilemmaManagerFactory() : FeatureContainerFactory<FeatureContainer> {

  companion object : PrisonersDilemmaManagerFactory() {

    override val rewardSupplier: PrisonerRewardSupplier =
      PrisonerRewardSupplier.uniform(
        ItemReward(Material.DIAMOND, 2),
        ItemReward(Material.IRON_NUGGET, 8),
        ItemReward(Material.IRON_INGOT, 3),
        ItemReward(Material.STONE_PICKAXE, 1),
        ItemReward(Material.OAK_LOG, 6),
        ItemReward(Material.COOKED_BEEF, 16),
        ItemReward(Material.COOKED_CHICKEN, 16),
        ItemReward(Material.WHITE_BED, 1),
        ItemReward(Material.SHEEP_SPAWN_EGG, 2),
        ItemReward(Material.VILLAGER_SPAWN_EGG, 1),
      )

  }

  abstract val rewardSupplier: PrisonerRewardSupplier

  open val responseTimeMinutes: Int
    get() = 2

  override fun create(state: BuilderState): FeatureContainer {
    val manager = PrisonersDilemmaManager(state.plugin, rewardSupplier, responseTimeMinutes)
    return FeatureBuilder()
      .addFeature(manager)
      .addCommand("split" to manager.splitCommand.withPermission(Permissions.PRISONER))
      .addCommand("steal" to manager.stealCommand.withPermission(Permissions.PRISONER))
      .addDebugCommand("prisonerjudge" to manager.dilemmaJudgeCommand)
      .addRandomEvent(manager.randomEvent)
      .build()
  }

}
