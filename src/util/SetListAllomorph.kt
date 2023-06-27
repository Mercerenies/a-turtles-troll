
package com.mercerenies.turtletroll.util

import java.util.Spliterator

// SetListAllomorph is a type which stores the same data as both a set
// and a list. This allows efficient .contains() access as well as
// ordered iteration.
sealed interface SetListAllomorph<out E> : Set<E>, List<E> {

  companion object {

    operator fun<E> invoke(elementsList: List<E>): SetListAllomorph<E> =
      FromList(elementsList)

    operator fun<E> invoke(elementsSet: Set<E>): SetListAllomorph<E> =
      FromSet(elementsSet)

    // Starting from a list preserves the order, guaranteed.
    fun<E> of(vararg elements: E): SetListAllomorph<E> =
      FromList(elements.toList())

  }

  private class FromSet<out E>(
    private val setImpl: Set<E>,
  ) : SetListAllomorph<E> {
    private val listImpl: List<E> by lazy { setImpl.toList() }

    override val size: Int
      get() = setImpl.size

    override fun isEmpty(): Boolean =
      setImpl.isEmpty()

    override fun toList() = listImpl
    override fun toSet() = setImpl

  }

  private class FromList<out E>(
    private val listImpl: List<E>,
  ) : SetListAllomorph<E> {
    private val setImpl: Set<E> by lazy { listImpl.toSet() }

    override val size: Int
      get() = listImpl.size

    override fun isEmpty(): Boolean =
      listImpl.isEmpty()

    override fun toList() = listImpl
    override fun toSet() = setImpl

  }

  abstract override val size: Int

  fun toList(): List<E>

  fun toSet(): Set<E>

  override fun contains(element: @UnsafeVariance E): Boolean =
    toSet().contains(element)

  override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean =
    toSet().containsAll(elements)

  // Always get iterator() from the list, so we get a consistent
  // ordering.
  override fun iterator(): Iterator<E> =
    toList().iterator()

  override fun listIterator(): ListIterator<E> =
    toList().listIterator()

  override fun spliterator(): Spliterator<@UnsafeVariance E> =
    toList().spliterator()

  override fun listIterator(index: Int): ListIterator<E> =
    toList().listIterator(index)

  override fun subList(fromIndex: Int, toIndex: Int): SetListAllomorph<E> =
    FromList(toList().subList(fromIndex, toIndex))

  override operator fun get(index: Int): E =
    toList().get(index)

  override fun indexOf(element: @UnsafeVariance E): Int =
    toList().indexOf(element)

  override fun lastIndexOf(element: @UnsafeVariance E): Int =
    toList().lastIndexOf(element)

}
