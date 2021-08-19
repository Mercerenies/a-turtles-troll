
package com.mercerenies.turtletroll.dripstone

import org.bukkit.block.Block

// Wrapper for Block that provides structural equals and hashCode.
class EqBlock(val block: Block) {

  override fun equals(other: Any?): Boolean {
    if ((other === null) || (other !is EqBlock)) {
      return false
    }
    return this.block.location == other.block.location
  }

  override fun hashCode(): Int =
    this.block.location.hashCode()

}
