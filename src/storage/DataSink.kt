
package com.mercerenies.turtletroll.storage

// A DataSink never returns any data and ignores any attempts to write
// to it. This is the trivial implementation of GlobalDataHolder.
object DataSink : GlobalDataHolder {

  override fun getData(key: String): String? = null

  override fun putData(key: String, value: String) {}

}
