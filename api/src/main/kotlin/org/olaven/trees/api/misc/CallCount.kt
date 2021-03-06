package org.olaven.trees.api.misc

/**
 * Implemented by controllers.
 *
 * Used to test caching.
 */
class CallCount (
        var getOne: Int = 0,
        var getAll: Int = 0
)

interface HasCallCount {
    companion object val callCount: CallCount
}