package name.mharbovskyi.satellitegame.domain.usecase

import name.mharbovskyi.satellitegame.domain.CoordinateTransformer
import name.mharbovskyi.satellitegame.domain.ScaledValues
import name.mharbovskyi.satellitegame.domain.entity.ObjectState

class GetTrajectoryUsecase(
    private val coordinateTransformer: CoordinateTransformer,
    private val scaledValues: ScaledValues,
    private val trajectoryBuilder: (ObjectState) -> Sequence<ObjectState>
) {
    fun get(satellite: ObjectState): Sequence<TrajectoryState> =
        trajectoryBuilder(satellite)
            .filterIndexed { index, _ -> index % ( scaledValues.frameDropRate ) == 0 }
            .map { it.location }
            .map { coordinateTransformer.transformLocation(it) }
            .map { Next(it) }

}