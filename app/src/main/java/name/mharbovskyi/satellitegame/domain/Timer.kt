package name.mharbovskyi.satellitegame.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.*

object CommonPool : Pool(ForkJoinPool.commonPool())

open class Pool(val pool: ForkJoinPool) : AbstractCoroutineContextElement(ContinuationInterceptor),
    ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        PoolContinuation(pool, continuation.context.fold(continuation) { cont, element ->
            if (element != this@Pool && element is ContinuationInterceptor)
                element.interceptContinuation(cont) else cont
        })

    // runs new coroutine in this pool in parallel (schedule to a different thread)
    fun runParallel(block: suspend () -> Unit) {
        pool.execute { launch(this, block) }
    }
}

private class PoolContinuation<T>(
    val pool: ForkJoinPool,
    val cont: Continuation<T>
) : Continuation<T> {
    override val context: CoroutineContext = cont.context

    override fun resumeWith(result: Result<T>) {
        pool.execute { cont.resumeWith(result) }
    }
}

fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
    block.startCoroutine(Continuation(context) { result ->
        result.onFailure { exception ->
            val currentThread = Thread.currentThread()
            currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
        }
    })

// produce stream of intervals milliseconds at fixed rate
fun CoroutineScope.intervalTimer(interval: Long = 100L) = produce {
    while (true) {
        delay(interval)
        send(interval)
    }
}

fun CoroutineScope.accumulativeTimer(intervalChannel: ReceiveChannel<Long>) = produce {
    var passedTime = 0L
    for (interval in intervalChannel) {
        passedTime += interval
        send(passedTime)
    }
}

fun CoroutineScope.scaledTimer(scale: Double, timerChannel: ReceiveChannel<Long>) = produce {
    for (time in timerChannel) {
        send(time * scale)
    }
}

fun tick(millis: Long): ReceiveChannel<Long> {
    val c = Channel<Long>()
    go {
        while (true) {
            delay(millis)
            c.send(millis)
        }
    }
    return c
}

fun go(block: suspend () -> Unit) = CommonPool.runParallel(block)


