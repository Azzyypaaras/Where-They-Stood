package net.azzy.forgotten.util.context

import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class ContextMap<T, K : Enum<K>> internal constructor() {
    internal val CONSUMERS: HashMap<K, Consumer<T>?> = HashMap()

    //Returns true if execution was successful
    fun execute(consumerPackage: T, context: K): Boolean {
        val consumer = CONSUMERS[context]
        if (consumer != null) {
            consumer.accept(consumerPackage)
            return true
        }
        return false
    }

    fun executeWithFallback(consumerPackage: T, context: K, fallback: Consumer<T>) {
        if (!execute(consumerPackage, context)) {
            fallback.accept(consumerPackage)
        }
    }
}

fun <T, K : Enum<K>> construct(consumers: Array<ContextConsumer<T, K>>?): ContextMap<*, *> {
    val map = ContextMap<T, K>()
    if (consumers == null) return map
    for (consumer in consumers) {
        map.CONSUMERS[consumer.context] = consumer.consumer
    }
    return map
}