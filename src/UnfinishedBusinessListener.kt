
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.feature.AbstractFeature
import com.mercerenies.turtletroll.feature.container.FeatureContainer
import com.mercerenies.turtletroll.feature.container.ListenerContainer
import com.mercerenies.turtletroll.feature.builder.BuilderState
import com.mercerenies.turtletroll.feature.builder.FeatureContainerFactory

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDamageAbortEvent

class UnfinishedBusinessListener() : AbstractFeature(), Listener {

  companion object : FeatureContainerFactory<FeatureContainer> {

    override fun create(state: BuilderState): FeatureContainer =
      ListenerContainer(UnfinishedBusinessListener())

    private val messageList = listOf(
      "What? I'm not good enough for ya?",
      "Fine then! Don't mine me!",
      "Whatever! I'll just go!",
      "Yer never gonna catch me around these parts again!",
    )

    private fun chooseMessage(): String =
      messageList.random()

  }

  override val name = "unfinished"

  override val description = "If you start mining an ore and then stop, it gets mad and runs away"

  @EventHandler
  fun onBlockDamageAbort(event: BlockDamageAbortEvent) {
    if (!isEnabled()) {
      return
    }
    val blockType = event.block.type
    if (BlockTypes.ORES.contains(blockType)) {
      val blockName = AllItems.getName(blockType)
      val message = chooseMessage()
      Messages.sendMessage(event.player, "<${blockName}> ${message}")
      event.block.type = Material.COBWEB
    }
  }

}
