package name.mharbovskyi.satellitegame.domain.physics.calculation

import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Speed

fun nextLocation(
    satellite: ObjectState,
    timeInterval: Double
): Location =
    Location(
        x = nextLocationProjection(
            satellite.location.x,
            satellite.speed.x,
            timeInterval
        ),
        y = nextLocationProjection(
            satellite.location.y,
            satellite.speed.y,
            timeInterval
        )
    )

fun nextLocationSmooth(
    satellite: ObjectState,
    speed: Speed,
    timeInterval: Double
): Location {
    val speedX = (satellite.speed.x + speed.x) / 2
    val speedY = (satellite.speed.y + speed.y) / 2

    return Location(
        x = nextLocationProjection(
            satellite.location.x,
            speedX,
            timeInterval
        ),
        y = nextLocationProjection(
            satellite.location.y,
            speedY,
            timeInterval
        )
    )
}

internal fun nextLocationProjection(coordinate: Double, speed: Double, timeInterval: Double) =
    coordinate + speed * timeInterval