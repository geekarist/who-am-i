package me.cpele.whoami

class LiveEvent<T>(var value: T) {

    private var consumed: Boolean = false

    fun consume() {
        synchronized(consumed) {
            consumed = true
        }
    }

    fun isUnconsumed(): Boolean {
        synchronized(consumed) {
            return !consumed
        }
    }

}
