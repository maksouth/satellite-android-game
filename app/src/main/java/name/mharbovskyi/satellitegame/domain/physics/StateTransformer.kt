package name.mharbovskyi.satellitegame.domain.physics

import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.PlanetSystem
import name.mharbovskyi.satellitegame.domain.physics.calculation.nextAcceleration
import name.mharbovskyi.satellitegame.domain.physics.calculation.nextLocationSmooth
import name.mharbovskyi.satellitegame.domain.physics.calculation.nextSpeedSmooth

interface StateTransformer {
    fun nextSystem(planetSystem: PlanetSystem, timeStep: Double): PlanetSystem
    fun nextObjectState(planetSystem: PlanetSystem, satellite: ObjectState, timeStep: Double): ObjectState
}

class SinglePlanetStateTransformer(
    private val g: Double
): StateTransformer {

    override fun nextSystem(planetSystem: PlanetSystem, timeStep: Double): PlanetSystem =
        planetSystem

    override fun nextObjectState(
        planetSystem: PlanetSystem,
        satellite: ObjectState,
        timeStep: Double
    ): ObjectState {
        val acceleration = nextAcceleration(
            satellite,
            planetSystem.planets[0],
            g
        )
        val speed =
            nextSpeedSmooth(satellite, acceleration, timeStep)
        val location =
            nextLocationSmooth(satellite, speed, timeStep)

        return ObjectState(speed, acceleration, location)
    }
}

