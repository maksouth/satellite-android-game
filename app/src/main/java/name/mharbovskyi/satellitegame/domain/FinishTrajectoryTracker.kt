package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.distanceTo

fun implicitHasCollisionFactory(planet: Planet): (Location) -> Boolean =
    { hasCollision(planet, it) }

fun hasCollision(planet: Planet, location: Location): Boolean =
    planet.location.distanceTo(location) <= planet.radius

fun implicitIsFinishFactory(targer: Location, targetRadius: Double): (Location) -> Boolean =
    { satelliteLocation -> isFinish(targer, targetRadius, satelliteLocation) }

fun isFinish(target: Location, targetRadius: Double, satelliteLocation: Location): Boolean =
        target.distanceTo(satelliteLocation) <= targetRadius