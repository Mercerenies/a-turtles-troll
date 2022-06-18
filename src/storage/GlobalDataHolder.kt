
package com.mercerenies.turtletroll.storage

interface GlobalDataHolder {

  fun getData(key: String): String?

  fun putData(key: String, value: String)

}
