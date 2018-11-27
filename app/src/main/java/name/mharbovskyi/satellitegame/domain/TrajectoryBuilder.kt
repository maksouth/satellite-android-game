package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.physics.calculation.nextObjectState

fun trajectorySequenceBuilder(
    planet: Planet,
    gravitationalConstant: Double
): (ObjectState, Double) -> Sequence<ObjectState> = { satellite, timeStep ->

    generateSequence(satellite) {
        nextObjectState(
            it,
            planet,
            timeStep,
            gravitationalConstant
        )
    }

}