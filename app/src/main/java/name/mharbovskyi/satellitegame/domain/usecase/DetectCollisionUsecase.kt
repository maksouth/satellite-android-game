package name.mharbovskyi.satellitegame.domain.usecase

import name.mharbovskyi.satellitegame.domain.entity.Location

class DetectCollisionUsecase(
    private val hasCollision: (Location) -> Boolean
) {
    fun detect(trajectory: Sequence<TrajectoryState>): Sequence<TrajectoryState> = sequence {
        for (location in trajectory) {
            if (hasCollision(location)) {
                yield(Collision(location))
                break
            } else Next(location)
        }
    }
}