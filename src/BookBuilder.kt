
package com.mercerenies.turtletroll

import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

import net.kyori.adventure.text.Component

class BookBuilder(
  private val title: Component,
  private val author: Component,
  private val generation: BookMeta.Generation = BookMeta.Generation.ORIGINAL,
) {

  companion object {

    fun makeBook(title: Component, author: Component, generation: BookMeta.Generation, pages: List<Component>): ItemStack {
      val builder = BookBuilder(title, author, generation)
      builder.addPages(pages)
      return builder.build()
    }

    fun makeBook(title: Component, author: Component, pages: List<Component>): ItemStack =
      makeBook(title, author, BookMeta.Generation.ORIGINAL, pages)

  }

  private val pages: MutableList<Component> = ArrayList()

  fun addPage(page: Component) {
    pages.add(page)
  }

  fun addPages(pages: Iterable<Component>) {
    for (page in pages) {
      addPage(page)
    }
  }

  fun build(): ItemStack {
    val stack = ItemStack(Material.WRITTEN_BOOK, 1)
    val meta = stack.itemMeta!! as BookMeta
    meta.title(title)
    meta.author(author)
    meta.generation = generation
    for (page in pages) {
      meta.addPages(page)
    }
    stack.itemMeta = meta
    return stack
  }

}
