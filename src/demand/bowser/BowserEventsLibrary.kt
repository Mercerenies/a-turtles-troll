
package com.mercerenies.turtletroll.demand.bowser

import com.mercerenies.turtletroll.demand.event.EventSelector
import com.mercerenies.turtletroll.demand.event.WeightedListEventSelector

import org.bukkit.Material

class BowserEventsLibrary(
  val rewardsPool: List<BowserReward>,
) {

  companion object {

    val DEFAULT = BowserEventsLibrary(RewardsPool.ALL_REWARDS)

  }

  val allEvents: List<() -> BowserEvent> = listOf(
    BowserMiningEvent.miningEventFactory(
      rewardsPool = rewardsPool,
      material = Material.DIRT,
      requiredAmountPerPlayer = 100,
      requiredAmountNoise = 50,
    ),
    BowserMiningEvent.miningEventFactory(
      rewardsPool = rewardsPool,
      material = Material.STONE,
      requiredAmountPerPlayer = 30,
      requiredAmountNoise = 10,
    ),
  )

  val eventSelector: EventSelector =
    WeightedListEventSelector.uniform(allEvents)

}
