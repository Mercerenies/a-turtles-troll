
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.drop.BlockBreakEventListener
import com.mercerenies.turtletroll.drop.NullAction
import com.mercerenies.turtletroll.drop.ReplaceDropsAction
import com.mercerenies.turtletroll.drop.CancelDropAction
import com.mercerenies.turtletroll.drop.EndermiteSpawnAction
import com.mercerenies.turtletroll.drop.filter
import com.mercerenies.turtletroll.drop.asFeature
import com.mercerenies.turtletroll.drop.nearby.SilverfishAttackAction
import com.mercerenies.turtletroll.drop.nearby.StrongholdSilverfishAttackAction
import com.mercerenies.turtletroll.drop.nearby.BeeAttackAction
import com.mercerenies.turtletroll.drop.nearby.NetherrackBoomAction
import com.mercerenies.turtletroll.feature.Feature
import com.mercerenies.turtletroll.feature.CompositeFeature

import org.bukkit.inventory.ItemStack
import org.bukkit.event.Listener
import org.bukkit.Material

class BlockBreakEvents {

  private val regularDirtDrop = REGULAR_DIRT_DROP.asFeature("dirt1", "")
  private val frequentDirtDrop = FREQUENT_DIRT_DROP.asFeature("dirt2", "")
  private val dirtDropFeature = CompositeFeature(
    "dirtstacks",
    "Replaces drops with dirt stacks at random",
    listOf(regularDirtDrop, frequentDirtDrop),
  )
  private val silverfishAttackAction = SilverfishAttackAction().asFeature(
    "silverfish",
    "Breaking stone blocks will sometimes result in a silverfish attack",
  )
  private val strongholdAttackAction = StrongholdSilverfishAttackAction().asFeature(
    "stronghold",
    "Breaking stone bricks will always result in a silverfish attack",
  )
  private val beeAttackAction = BeeAttackAction().asFeature(
    "bees",
    "Breaking wood will sometimes result in a bee attack",
  )
  private val endermiteSpawnAction = EndermiteSpawnAction.asFeature(
    "endermites",
    "End stones will always spawn endermites when broken",
  )
  private val netherrackBoomAction = NetherrackBoomAction().asFeature(
    "netherrack",
    "Common nether materials cause a cascading effect, breaking nearby blocks when broken",
  )
  private val cancelDropAction = CancelDropAction.filter {
    NO_DROP_ON.contains(it.block.type)
  }.asFeature(
    "nodrops",
    "Several block types refuse to drop when broken",
  )

  private val breakOverrides = listOf(
    endermiteSpawnAction,
    netherrackBoomAction,
    cancelDropAction,
    strongholdAttackAction,
  )

  private val breakEvents = listOf(
    Weight(NullAction, 1.0),
    Weight(regularDirtDrop, 0.3),
    Weight(frequentDirtDrop, 1.0),
    Weight(silverfishAttackAction, 0.2),
    Weight(beeAttackAction, 0.2),
  )

  val listener = BlockBreakEventListener(overrideRules = breakOverrides, actions = breakEvents)

  companion object {

    val NO_DROP_ON = arrayOf(Material.CRAFTING_TABLE, Material.FURNACE,
                             Material.SMOKER, Material.BLAST_FURNACE)

    val FREQUENT_DIRT_DROP_TRIGGERS = setOf(
      Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
      Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
      Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS
    )

    val REGULAR_DIRT_DROP = ReplaceDropsAction(ItemStack(Material.DIRT, 64))
    val FREQUENT_DIRT_DROP = REGULAR_DIRT_DROP.filter { FREQUENT_DIRT_DROP_TRIGGERS.contains(it.block.type) }

  }

  fun getFeatures(): List<Feature> = listOf(
    dirtDropFeature, silverfishAttackAction, beeAttackAction,
    endermiteSpawnAction, netherrackBoomAction, cancelDropAction,
    strongholdAttackAction,
  )

}
