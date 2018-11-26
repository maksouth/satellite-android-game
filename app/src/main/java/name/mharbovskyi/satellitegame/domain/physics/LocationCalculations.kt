package name.mharbovskyi.satellitegame.domain.physics

import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState

fun nextLocation(
    satellite: ObjectState,
    timeInterval: Double
): Location =
    Location(
        x = nextLocationProjection(satellite.location.x, satellite.speed.x, timeInterval),
        y = nextLocationProjection(satellite.location.y, satellite.speed.y, timeInterval)
    )

//todo add speed averaging 0.5 *( vx1+vx2)
fun nextLocationProjection(coordinate: Double, speed: Double, timeInterval: Double) =
    coordinate + speed * timeInterval