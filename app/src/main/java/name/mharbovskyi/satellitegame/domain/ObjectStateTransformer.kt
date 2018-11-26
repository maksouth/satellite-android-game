package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.physics.nextObjectState

fun objectStateTransformer(planet: Planet): (ObjectState, Double) -> ObjectState =
    {satellite, timeInterval -> nextObjectState(satellite, planet, timeInterval)}