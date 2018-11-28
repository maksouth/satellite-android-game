package name.mharbovskyi.satellitegame.domain.usecase

import name.mharbovskyi.satellitegame.domain.entity.Location

class DetectFinishUsecase(
   private val isFinish: (Location) -> Boolean
) {

}

sealed class TrajectoryState
data class Next(val location: Location): TrajectoryState()
data class Collision(val location: Location): TrajectoryState()
data class Finish(val location: Location): TrajectoryState()