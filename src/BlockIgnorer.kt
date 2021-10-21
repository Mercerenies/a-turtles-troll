
package com.mercerenies.turtletroll

import com.mercerenies.turtletroll.dripstone.EqBlock

import org.bukkit.block.Block

interface BlockIgnorer {

  fun ignore(block: Block)

  object Null : BlockIgnorer {
    override fun ignore(block: Block) {}
  }

  class MemoryIgnorer(
    private val memory: CooldownMemory<EqBlock>,
    val ticks: Long,
  ) : BlockIgnorer {

    override fun ignore(block: Block) {
      memory.add(EqBlock(block), ticks)
    }

  }

}
