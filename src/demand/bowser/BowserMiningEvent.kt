
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.AllItems
import com.mercerenies.turtletroll.demand.GodsState

import org.bukkit.Material
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.entity.Player

import net.kyori.adventure.text.Component

import kotlin.random.Random
import kotlin.math.max

class BowserMiningEvent(
  val rewardsPool: List<BowserReward>,
  val blockPredicate: (Block) -> Boolean,
  val blockName: String,
  val requiredAmount: Int,
) : BowserEvent() {

  companion object {

    fun miningEventFactory(
      rewardsPool: List<BowserReward>,
      material: Material,
      requiredAmountPerPlayer: Int,
      requiredAmountNoise: Int,
    ): () -> BowserMiningEvent =
      {
        val onlinePlayers = Bukkit.getOnlinePlayers().size
        val requiredAmount = requiredAmountPerPlayer * onlinePlayers + Random.nextInt(- requiredAmountNoise, requiredAmountNoise)
        BowserMiningEvent(
          rewardsPool = rewardsPool,
          material = material,
          requiredAmount = max(requiredAmount, 1),
        )
      }

  }

  constructor(rewardsPool: List<BowserReward>, materials: Collection<Material>, blockName: String, requiredAmount: Int) :
    this(rewardsPool, { block -> materials.contains(block.type) }, blockName, requiredAmount)

  constructor(rewardsPool: List<BowserReward>, material: Material, blockName: String, requiredAmount: Int) :
    this(rewardsPool, listOf(material), blockName, requiredAmount)

  constructor(rewardsPool: List<BowserReward>, material: Material, requiredAmount: Int) :
    this(rewardsPool, material, AllItems.getName(material), requiredAmount)

  private var amountMined: Int = 0

  override val summary: String =
    "Mine ${requiredAmount} ${blockName}"

  override fun getRequestMessage(): Component =
    bowserMessage("Less talking, more mining! You guys have only mined ${amountMined} ${blockName.lowercase()}. I want to see ${requiredAmount} of them mined by the end of the day!")

  override fun getDayStartMessage(): Component =
    bowserMessage("Alright, you lazy block-shaped whatevers! I want to see some mining! Get to mining ${blockName.lowercase()}. I want to see ${requiredAmount} of them mined by the end of the day!")

  override fun onDaytimeBlockBreak(event: BlockBreakEvent, godsState: GodsState) {
    if (blockPredicate(event.block)) {
      incrementAmountMined(godsState, event.player)
    }
  }

  private fun incrementAmountMined(godsState: GodsState, player: Player) {
    if (amountMined >= requiredAmount) {
      return
    }

    amountMined += 1
    if (amountMined >= requiredAmount) {
      // We just met the condition
      godsState.setGodsAppeased(true)
      BowserReward.deliverRewardFromPool(rewardsPool, player)
    }
  }

}
