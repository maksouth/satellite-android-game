package name.mharbovskyi.satellitegame.physics

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// produce stream of intervals milliseconds at fixed rate
fun CoroutineScope.intervalTimer(interval: Long = 100L) = produce {
    var passedTime = 0L
    while (true) {
        passedTime += interval
        delay(interval)
        send(passedTime)
    }
}

fun CoroutineScope.accumulativeTimer(intervalChannel: ReceiveChannel<Long>) = produce {
    var passedTime = 0L
    for (interval in intervalChannel) {
        passedTime += interval
        send(passedTime)
    }
}