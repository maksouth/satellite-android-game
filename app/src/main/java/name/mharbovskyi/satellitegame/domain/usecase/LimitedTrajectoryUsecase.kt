package name.mharbovskyi.satellitegame.domain.usecase

import name.mharbovskyi.satellitegame.domain.CompletionTracker
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.PlanetSystem
import name.mharbovskyi.satellitegame.domain.entity.Target
import name.mharbovskyi.satellitegame.domain.TrajectoryBuilder

class LimitedTrajectoryUsecase(
    private val trajectoryBuilder: TrajectoryBuilder,
    private val completionTracker: CompletionTracker,
    private val dropRate: Int,
    private val timeStep: Double
) {
    fun build(
        planetSystem: PlanetSystem,
        satellite: ObjectState,
        target: Target,
        collisionListener: (Location) -> Unit,
        finishListener: (Location) -> Unit
    ): Sequence<Pair<PlanetSystem, ObjectState>> =
        trajectoryBuilder
            .build(planetSystem, satellite, timeStep)
            .filterIndexed{ index, _ -> index % dropRate == 0 }
            .takeWhile { (_planetSystem, _satellite) ->
                checkAndNotifyCompletion(
                    _planetSystem,
                    target,
                    _satellite,
                    collisionListener,
                    finishListener
                )
            }

    private fun checkAndNotifyCompletion(
        planetSystem: PlanetSystem,
        target: Target,
        satellite: ObjectState,
        collisionListener: (Location) -> Unit,
        finishListener: (Location) -> Unit
    ): Boolean =
        when {
            completionTracker.isFinish(target, satellite) -> {
                finishListener(satellite.location)
                false
            }
            completionTracker.isCollision(planetSystem, satellite) -> {
                collisionListener(satellite.location)
                false
            }
            else -> true
        }
}