package me.cpele.whoami

class LiveEvent<T>(private var _value: T? = null) {

    val value: T?
        get() = if (consumed) null else _value

    private var consumed: Boolean = false

    fun consume() {
        consumed = true
    }
}
