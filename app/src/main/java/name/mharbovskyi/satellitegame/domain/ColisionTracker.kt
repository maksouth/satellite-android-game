package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.distanceTo

fun hasCollision(planet: Planet, satellite: ObjectState): Boolean =
    planet.location.distanceTo(satellite.location) <= planet.radius