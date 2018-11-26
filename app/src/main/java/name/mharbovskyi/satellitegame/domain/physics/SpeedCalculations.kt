package name.mharbovskyi.satellitegame.domain.physics

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.Speed
import kotlin.math.sqrt

fun nextSpeed(satellite: ObjectState, timeInterval: Double): Speed =
    Speed(
        x = nextSpeedProjection(satellite.speed.x, satellite.acceleration.x, timeInterval),
        y = nextSpeedProjection(satellite.speed.y, satellite.acceleration.y, timeInterval)
    )

//todo add acceleration averaging 0.5 *( ax1+ax2)
fun nextSpeedProjection(speed: Double, acceleration: Double, timeInterval: Double) =
    speed + acceleration * timeInterval

fun primaryOrbitSpeed(planet: Planet) =
    sqrt(G * planet.mass / planet.radius)