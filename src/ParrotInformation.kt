
package com.mercerenies.turtletroll

// Right now, we just use Booleans to indicate "yes" or "no". We might
// try to extract more info later, but for now that's good enough.
interface ParrotInformation {

  fun hasLeftShoulderPerch(): Boolean

  fun hasRightShoulderPerch(): Boolean

  fun hasAnyShoulderPerch(): Boolean =
    hasLeftShoulderPerch() || hasRightShoulderPerch()

}
