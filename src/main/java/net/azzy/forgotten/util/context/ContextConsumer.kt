package net.azzy.forgotten.util.context

import java.util.function.Consumer

class ContextConsumer<T, K : Enum<K>> internal constructor(val consumer: Consumer<T>, val context: K) {

    fun accept(t: T) {
        consumer.accept(t)
    }
}

fun <T, K : Enum<K>> of(consumer: Consumer<T>, context: K): ContextConsumer<*, *> {
    return ContextConsumer(consumer, context)
}