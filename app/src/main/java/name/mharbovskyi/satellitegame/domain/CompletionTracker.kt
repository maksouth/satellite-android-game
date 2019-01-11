package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot

interface CompletionTracker {
    fun isFinish(target: TargetSpot, satellite: ObjectState): Boolean
    fun isCollision(planetSystem: PlanetSystem, satellite: ObjectState): Boolean
}

class CompletionTrackerImpl: CompletionTracker {

    override fun isFinish(target: TargetSpot, satellite: ObjectState) =
        target.location.distanceTo(satellite.location) <= target.radius

    override fun isCollision(planetSystem: PlanetSystem, satellite: ObjectState) =
        planetSystem.planets.any {
            it.location.distanceTo(satellite.location) <= it.radius
        }
}