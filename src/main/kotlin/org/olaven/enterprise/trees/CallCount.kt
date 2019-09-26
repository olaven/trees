package org.olaven.enterprise.trees

class CallCount (
        var getOne: Int = 0,
        var getAll: Int = 0
)

interface HasCallCount {
    companion object val callCount: CallCount
}