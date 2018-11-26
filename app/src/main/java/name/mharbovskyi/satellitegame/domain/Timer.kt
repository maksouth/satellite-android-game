package name.mharbovskyi.satellitegame.domain

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.math.roundToLong

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
        send((time * scale).roundToLong())
    }
}