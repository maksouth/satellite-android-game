package name.mharbovskyi.satellitegame.domain.physics

import name.mharbovskyi.satellitegame.domain.entity.*
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * distance - m
 * time -s
 */

const val G = 6.67e-11 // (m^3) / (kg * s^2)

fun nextObjectState(
    satellite: ObjectState,
    planet: Planet,
    timeInterval: Double,
    gravitationalConstant: Double = G
): ObjectState {

    val acceleration = nextAcceleration(satellite, planet, gravitationalConstant)
    val speed = nextSpeedSmooth(satellite, acceleration, timeInterval)
    val location = nextLocationSmooth(satellite, speed, timeInterval)

    return ObjectState(speed, acceleration, location)
}