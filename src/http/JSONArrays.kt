
package com.mercerenies.turtletroll.http

import org.json.JSONArray

// Helpers for asserting the types of elements in JSONArray. Methods
// here throw org.json.JSONException when assertions fail.
object JSONArrays {

  // Takes a function that maps from index to the target type.
  // Usually, this will be a bound method from JSONArray, such as
  // JSONArray::getJSONObject.
  inline fun<R> toTypedList(array: JSONArray, accessor: (Int) -> R): List<R> {
    val indices = 0 until array.length()
    return indices.toList().map(accessor)
  }

}
