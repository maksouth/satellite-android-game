package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.physics.calculation.nextObjectState

fun trajectorySequenceBuilder(
    planet: Planet,
    gravitationalConstant: Double,
    timeStep: Double
): (ObjectState) -> Sequence<ObjectState> = { satellite ->

    generateSequence(satellite) { previousState ->
        nextObjectState(previousState, planet, timeStep, gravitationalConstant)
    }

}