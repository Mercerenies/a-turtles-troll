
package com.mercerenies.turtletroll

import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class BookBuilder(
  private val title: String,
  private val author: String,
  private val generation: BookMeta.Generation = BookMeta.Generation.ORIGINAL,
) {

  companion object {

    fun makeBook(title: String, author: String, generation: BookMeta.Generation, pages: List<String>): ItemStack {
      val builder = BookBuilder(title, author, generation)
      builder.addPages(pages)
      return builder.build()
    }

    fun makeBook(title: String, author: String, pages: List<String>): ItemStack =
      makeBook(title, author, BookMeta.Generation.ORIGINAL, pages)

  }

  private val pages: MutableList<String> = ArrayList()

  fun addPage(page: String) {
    pages.add(page)
  }

  fun addPages(pages: Iterable<String>) {
    for (page in pages) {
      addPage(page)
    }
  }

  fun build(): ItemStack {
    val stack = ItemStack(Material.WRITTEN_BOOK, 1)
    val meta = stack.itemMeta!! as BookMeta
    meta.title = title
    meta.author = author
    meta.generation = generation
    meta.setPages(pages)
    stack.itemMeta = meta
    return stack
  }

}
