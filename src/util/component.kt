
package com.mercerenies.turtletroll.util.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.BuildableComponent

// Helpers for the net.kyori component types

fun pluralize(items: List<Component>, conjunction: String = "and"): Component =
  when (items.size) {
    0 -> Component.text("")
    1 -> items[0]
    2 -> items[0].append(" ${conjunction} ").append(items[1])
    else -> {
      val itemCount = items.size
      val result = Component.text()
      for ((i, s) in items.withIndex()) {
        if (i == 0) {
          result.append(s)
        } else if (i == itemCount - 1) {
          result.append(", ${conjunction} ").append(s)
        } else {
          result.append(", ").append(s)
        }
      }
      result.build()
    }
  }

fun Component.asPlainText(): String =
  PlainTextComponentSerializer.plainText().serialize(this)

fun joinWithCommas(components: List<Component>): Component {
  var first = true
  val builder = Component.text()
  for (component in components) {
    if (first) {
      first = false
    } else {
      builder.append(", ")
    }
    builder.append(component)
  }
  return builder.build()
}

fun Component.append(text: String): Component =
  this.append(Component.text(text))

fun<C : BuildableComponent<C, B>, B : ComponentBuilder<C, B>> ComponentBuilder<C, B>.append(text: String): B =
  this.append(Component.text(text))
