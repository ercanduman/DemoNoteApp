package com.enbcreative.demonoteapp.utils

import kotlinx.coroutines.*

fun <T> lazyDeferred(work: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy { GlobalScope.async(start = CoroutineStart.LAZY) { work.invoke(this) } }
}