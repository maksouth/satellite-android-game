package name.mharbovskyi.satellitegame.domain.usecase

import name.mharbovskyi.satellitegame.domain.entity.Location

class LimitTrajectoryUsecase(
    private val hasCollision: (Location) -> Boolean,
    private val isFinish: (Location) -> Boolean
) {
    fun detect(trajectory: Sequence<Location>): Sequence<TrajectoryState> = sequence {
        for (location in trajectory) {
            if (isFinish(location)) {
                yield(Finish(location))
                break
            } else if (hasCollision(location)) {
                yield(Collision(location))
                break
            } else Next(location)
        }
    }
}

sealed class TrajectoryState(val location: Location)
class Next(location: Location): TrajectoryState(location)
class Collision(location: Location): TrajectoryState(location)
class Finish(location: Location): TrajectoryState(location)