package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.PlanetSystem
import name.mharbovskyi.satellitegame.domain.physics.StateTransformer

interface TrajectoryBuilder {
    fun build(
        planetSystem: PlanetSystem,
        satellite: ObjectState,
        timeStep: Double
    ): Sequence<Pair<PlanetSystem, ObjectState>>
}

class TrajectoryBuilderImpl(
    private val stateTransformer: StateTransformer
): TrajectoryBuilder {

    override fun build(
        planetSystem: PlanetSystem,
        satellite: ObjectState,
        timeStep: Double
    ): Sequence<Pair<PlanetSystem, ObjectState>> = sequence {
        var _planetSystem = planetSystem
        var _satellite = satellite
        while (true) {
            _planetSystem = stateTransformer.nextSystem(_planetSystem, timeStep)
            _satellite = stateTransformer.nextObjectState(_planetSystem, _satellite, timeStep)
            yield(Pair(_planetSystem, _satellite))
        }
    }
}