package name.mharbovskyi.satellitegame.domain.physics

import name.mharbovskyi.satellitegame.domain.entity.*
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * distance - m
 * time -s
 */

const val G = 6.67e-11 // (m^3) / (kg * s^2)

fun primaryOrbitSpeed(planet: Planet) =
        sqrt(G * planet.mass / planet.radius)

fun nextObjectState(
    satellite: ObjectState,
    planet: Planet,
    timeInterval: Long
): ObjectState =
    ObjectState(
        speed = nextSpeed(satellite, timeInterval),
        location = nextLocation(satellite, timeInterval),
        acceleration = nextAcceleration(satellite, planet)
    )

fun nextSpeed(satellite: ObjectState, timeInterval: Long): Speed =
    Speed(
        x = nextSpeedProjection(satellite.speed.x, satellite.acceleration.x, timeInterval),
        y = nextSpeedProjection(satellite.speed.y, satellite.acceleration.y, timeInterval)
    )

fun nextSpeedProjection(speed: Double, acceleration: Double, timeInterval: Long) =
    speed + acceleration * timeInterval

fun nextAcceleration(
    satellite: ObjectState,
    planet: Planet
): Acceleration {

    val distance = satellite.location.distanceTo(planet.location)
    return Acceleration(
        calculateAccelerationProjection(planet.mass, planet.location.x, satellite.location.x, distance),
        calculateAccelerationProjection(planet.mass, planet.location.y, satellite.location.y, distance)
    )
}

fun calculateAccelerationProjection(
    gravitationalCenterMass: Double,
    gravitationalCenterProjection: Double,
    satelliteProjection: Double,
    distance: Double,
    gravitationalConstant: Double = G,
    distancePower: Double = 3.0
): Double {
    val projectionDistance = gravitationalCenterProjection - satelliteProjection
    return  -gravitationalConstant * gravitationalCenterMass * projectionDistance / distance.pow(distancePower)
}

fun nextLocation(
    satellite: ObjectState,
    timeInterval: Long
): Location =
    Location(
        x = nextLocationProjection(satellite.location.x, satellite.speed.x, timeInterval),
        y = nextLocationProjection(satellite.location.y, satellite.speed.y, timeInterval)
    )

fun nextLocationProjection(coordinate: Double, speed: Double, timeInterval: Long) =
        coordinate + speed * timeInterval

fun Location.distanceTo(other: Location) =
        sqrt( (x - other.x).pow(2) + (y - other.y).pow(2) )