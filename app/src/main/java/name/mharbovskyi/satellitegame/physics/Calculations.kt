package name.mharbovskyi.satellitegame.physics

import name.mharbovskyi.satellitegame.physics.entity.*
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
    timeInterval: Double
): ObjectState =
    ObjectState(
        speed = nextSpeed(satellite, timeInterval),
        location = nextLocation(satellite, timeInterval),
        acceleration = nextAcceleration(satellite, planet, timeInterval)
    )

fun nextSpeed(satellite: ObjectState, timeInterval: Double): Speed =
    Speed(
        x = nextSpeedProjection(satellite.speed.x, satellite.acceleration.x, timeInterval),
        y = nextSpeedProjection(satellite.speed.x, satellite.acceleration.x, timeInterval)
    )

fun nextSpeedProjection(speed: Double, acceleration: Double, timeInterval: Double) =
    speed + acceleration * timeInterval

fun nextAcceleration(
    satellite: ObjectState,
    planet: Planet,
    timeInterval: Double
): Acceleration {

    val distance = satellite.distanceTo(planet)
    return Acceleration(
        calculateAccelerationProjection(planet.mass, planet.location.x, satellite.location.x, distance, timeInterval),
        calculateAccelerationProjection(planet.mass, planet.location.y, satellite.location.y, distance, timeInterval)
    )
}

fun calculateAccelerationProjection(
    gravitationalCenterMass: Double,
    gravitationalCenterProjection: Double,
    satelliteProjection: Double,
    distance: Double,
    timeInterval: Double,
    gravitationalConstant: Double = G,
    distancePower: Double = 3.0
): Double {
    val projectionDistance = gravitationalCenterProjection - satelliteProjection
    return gravitationalConstant * gravitationalCenterMass * satelliteProjection * timeInterval / distance.pow(distancePower)
}

fun nextLocation(
    satellite: ObjectState,
    timeInterval: Double
): Location =
    Location(
        x = nextLocationProjection(satellite.location.x, satellite.speed.x, timeInterval),
        y = nextLocationProjection(satellite.location.y, satellite.speed.y, timeInterval)
    )

fun nextLocationProjection(coordinate: Double, speed: Double, timeInterval: Double) =
        coordinate + speed * timeInterval

fun ObjectState.distanceTo(other: ObjectState) =
        sqrt( (location.x - other.location.x).pow(2) + (location.y - other.location.y).pow(2) )