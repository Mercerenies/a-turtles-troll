
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.RunnableFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.RunnableContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory
import com.mercerenies.turtletroll.ext.*

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.inventory.ItemStack

class BucketRouletteRunnable(plugin: Plugin) : RunnableFeature(plugin) {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      RunnableContainer(BucketRouletteRunnable(state.plugin))

    private fun shouldCycle(itemStack: ItemStack): Boolean {
      if (!BlockTypes.BUCKETS.contains(itemStack.type)) {
        return false
      }
      val itemMeta = itemStack.itemMeta
      if (itemMeta == null) {
        return false
      }
      return !itemMeta.hasDisplayName()
    }

    private fun cycleBucketType(itemStack: ItemStack) {
      var newBucketType = BlockTypes.BUCKETS.sample()!!
      while (newBucketType == itemStack.type) {
        newBucketType = BlockTypes.BUCKETS.sample()!!
      }
      itemStack.type = newBucketType
    }

  }

  override val name: String = "bucketroulette"

  override val description: String = "Unnamed buckets cycle their contents every eight seconds"

  override val taskPeriod = Constants.TICKS_PER_SECOND * 8L

  override fun run() {
    if (!isEnabled()) {
      return
    }
    val onlinePlayers = Bukkit.getOnlinePlayers().toSet()
    for (player in onlinePlayers) {
      // TODO (HACK) Kotlin can't parse the more complex nullability
      // annotation on getContents(), so it gets the wrong idea.
      for (itemStack in player.inventory.contents!!) {
        if ((itemStack != null) && (shouldCycle(itemStack))) {
          cycleBucketType(itemStack)
        }
      }
    }
  }

}
