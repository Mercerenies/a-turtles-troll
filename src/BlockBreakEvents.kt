
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.drop.BlockBreakEventListener
import com.mercerenies.turtletroll.drop.BlockBreakAction
import com.mercerenies.turtletroll.drop.ShuffleDropsAction
import com.mercerenies.turtletroll.drop.NullAction
import com.mercerenies.turtletroll.drop.ReplaceDropsAction
import com.mercerenies.turtletroll.drop.CancelDropAction
import com.mercerenies.turtletroll.drop.BedrockAction
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
  private val amethystBlockDrop = ReplaceDropsAction(ItemStack(Material.GOLDEN_APPLE, 1)).filter {
    it.block.type == Material.AMETHYST_BLOCK
  }.asFeature(
    "amethyst",
    "Amethyst blocks have a small chance of dropping golden apples when mined",
  )
  val cancelDropAction = CancelDropAction.filter { // Public because we're going to compose it in AllPluginListeners
    NO_DROP_ON.contains(it.block.type)
  }.asFeature(
    "nodrops",
    "Several block types refuse to drop when broken",
  )
  private val bedrockAction = BedrockAction.filter {
    BEDROCK_BLOCKS.contains(it.block.type)
  }.asFeature(
    "bedrock",
    "Several block types transform into bedrock when mined",
  )

  // TODO ShuffleDropsAction for stairs/slabs made of wood as well
  private val shuffleLogsAction = ShuffleDropsAction(BlockTypes.LOGS.toList()).asFeature("", "")
  private val shufflePlanksAction = ShuffleDropsAction(BlockTypes.PLANKS.toList()).asFeature("", "")

  private val replaceMelonsAction = ReplaceDropsAction(ItemStack(Material.PUMPKIN)).filter {
    it.block.type == Material.MELON
  }.asFeature("replacemelons", "...")
  private val replacePumpkinsAction = ReplaceDropsAction(ItemStack(Material.MELON_SLICE, 4)).filter {
    it.block.type == Material.PUMPKIN
  }.asFeature("replacepumpkins", "...")

  // Public so we can compose it with the recipes
  val melonPumpkinsFeature = CompositeFeature(
    "melompkinsdrops",
    "Melons and pumpkins swap drops",
    listOf(replaceMelonsAction, replacePumpkinsAction),
  )

  private val shuffleFeature = CompositeFeature(
    "shufflelogs",
    "Wooden drops are shuffled",
    listOf(shuffleLogsAction, shufflePlanksAction),
  )

  private val breakOverrides = listOf(
    endermiteSpawnAction,
    netherrackBoomAction,
    cancelDropAction,
    bedrockAction,
    strongholdAttackAction,
    replaceMelonsAction,
    replacePumpkinsAction,
  )

  private val breakEvents = listOf(
    Weight(NullAction, 1.0),
    Weight(regularDirtDrop, 0.3),
    Weight(frequentDirtDrop, 1.0),
    Weight(silverfishAttackAction, 0.2),
    Weight(beeAttackAction, 0.2),
    Weight(amethystBlockDrop, 0.1),
  )

  private val breakPost: List<BlockBreakAction> = listOf(
    shuffleLogsAction,
    shufflePlanksAction,
  )

  val listener = BlockBreakEventListener(
    preRules = breakOverrides,
    actions = breakEvents,
    postRules = breakPost,
  )

  fun getFeatures(): List<Feature> = listOf(
    dirtDropFeature, silverfishAttackAction, beeAttackAction,
    endermiteSpawnAction, netherrackBoomAction, cancelDropAction,
    strongholdAttackAction, bedrockAction, shuffleFeature, amethystBlockDrop,
  )

  companion object {

    val NO_DROP_ON = setOf(
      Material.CRAFTING_TABLE, Material.FURNACE,
      Material.SMOKER, Material.BLAST_FURNACE,
    )

    val FREQUENT_DIRT_DROP_TRIGGERS = setOf(
      Material.COAL_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
      Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
      Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS,
      Material.COPPER_ORE, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE,
      Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE,
      Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE,
    )

    val REGULAR_DIRT_DROP = ReplaceDropsAction(ItemStack(Material.DIRT, 64))
    val FREQUENT_DIRT_DROP = REGULAR_DIRT_DROP.filter { FREQUENT_DIRT_DROP_TRIGGERS.contains(it.block.type) }

    val BEDROCK_BLOCKS =
      ForestFireListener.BLOCKS + setOf(
        Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK,
        Material.DIORITE, Material.ANDESITE, Material.GRANITE,
        Material.BASALT, Material.BLACKSTONE,
      )

  }

}
