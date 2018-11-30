package name.mharbovskyi.satellitegame.domain.physics.calculation

import name.mharbovskyi.satellitegame.domain.entity.Acceleration
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.distanceTo
import kotlin.math.pow

fun nextAcceleration(
    satellite: ObjectState,
    planet: Planet,
    gravitationalConstant: Double
): Acceleration {

    val distance = satellite.location.distanceTo(planet.location)
    return Acceleration(
        calculateAccelerationProjection(
            planet.mass,
            planet.location.x,
            satellite.location.x,
            distance,
            gravitationalConstant
        ),
        calculateAccelerationProjection(
            planet.mass,
            planet.location.y,
            satellite.location.y,
            distance,
            gravitationalConstant
        )
    )
}

private fun calculateAccelerationProjection(
    gravitationalCenterMass: Double,
    gravitationalCenterProjection: Double,
    satelliteProjection: Double,
    distance: Double,
    gravitationalConstant: Double,
    distancePower: Double = 3.0
): Double {
    val projectionDistance = gravitationalCenterProjection - satelliteProjection
    return  gravitationalConstant * gravitationalCenterMass * projectionDistance / distance.pow(distancePower)
}