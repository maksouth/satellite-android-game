package name.mharbovskyi.satellitegame.domain.physics

import name.mharbovskyi.satellitegame.domain.entity.Acceleration
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.Speed
import kotlin.math.sqrt

fun nextSpeed(satellite: ObjectState, timeInterval: Double): Speed =
    Speed(
        x = nextSpeedProjection(satellite.speed.x, satellite.acceleration.x, timeInterval),
        y = nextSpeedProjection(satellite.speed.y, satellite.acceleration.y, timeInterval)
    )

fun nextSpeedSmooth(satellite: ObjectState, acceleration: Acceleration, timeInterval: Double): Speed {
    val accelerationX = (satellite.acceleration.x + acceleration.x) / 2
    val accelerationY = (satellite.acceleration.y + acceleration.y) / 2

    return Speed(
        x = nextSpeedProjection(satellite.speed.x, accelerationX, timeInterval),
        y = nextSpeedProjection(satellite.speed.y, accelerationY, timeInterval)
    )
}

internal fun nextSpeedProjection(speed: Double, acceleration: Double, timeInterval: Double) =
    speed + acceleration * timeInterval

fun primaryOrbitSpeed(planet: Planet) =
    sqrt(G * planet.mass / planet.radius)