package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.physics.nextObjectState

fun buildTrajectorySequence(
    satellite: ObjectState,
    planet: Planet, timeStep: Double,
    gravitationalConstant: Double
): Sequence<ObjectState> = generateSequence(satellite) {
    nextObjectState(it, planet, timeStep, gravitationalConstant = gravitationalConstant)
}