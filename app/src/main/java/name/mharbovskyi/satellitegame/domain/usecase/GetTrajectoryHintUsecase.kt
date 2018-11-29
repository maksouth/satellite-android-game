package name.mharbovskyi.satellitegame.domain.usecase

import name.mharbovskyi.satellitegame.domain.ScaledValues
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState

class GetTrajectoryHintUsecase(
    private val scaledValues: ScaledValues,
    private val trajectoryBuilder: (ObjectState) -> Sequence<ObjectState>
) {
    fun get(satellite: ObjectState): Sequence<Location> =
        trajectoryBuilder(satellite)
            .filterIndexed { index, _ -> index % ( scaledValues.frameDropRate * 10 ) == 0 }
            .take(20)
            .map { it.location }
}