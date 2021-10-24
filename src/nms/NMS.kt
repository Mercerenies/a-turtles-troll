
package com.mercerenies.turtletroll.nms

import org.bukkit.Bukkit
import org.bukkit.entity.FallingBlock

// https://riptutorial.com/bukkit/example/29589/accessing-the-current-minecraft-version :)
//
// I'm trying to leave detailed instructions on how to piece this
// together. When we inevitably have to update this for the next MC
// version (1.18), then it's going to be a pain. Hopefully I can
// alleviate that as much as possible.
object NMS {

  // This should never need to change.
  fun version(): String =
    Bukkit.getServer()::class.java.getPackage().getName().substring(23)

  // This should never need to change.
  fun getClass(suffix: String): Class<*> =
    Class.forName("org.bukkit.craftbukkit.${version()}.${suffix}")

  // Go to org.bukkit.craftbukkit.*.entity.CraftFallingBlock and look
  // at the implementation of canHurtEntities(). It returns a Boolean.
  // Remember the name of that Boolean (in 1.17, it's 'ap'). Now go to
  // net.minecraft.world.entity.item.EntityFallingBlock. There should
  // be a method with signature
  //
  // public void ???(float, int)
  //
  // Which sets that variable (again, `ap` in 1.17) to true and sets
  // two other variables to the arguments. We want to call that
  // function (`b` in 1.17) with 1.0f and 40.
  fun makeFallingBlockHurt(block: FallingBlock) {
    val cls = getClass("entity.CraftFallingBlock")
    val mcBlock = cls.getMethod("getHandle").invoke(block)
    Class.forName("net.minecraft.world.entity.item.EntityFallingBlock")
      .getMethod("b", java.lang.Float.TYPE, java.lang.Integer.TYPE)
      .invoke(mcBlock, 1.0f, 40)
  }

}
