package name.mharbovskyi.satellitegame.domain.physics.measurement

import kotlin.math.pow


interface MeasurementSystem {
    val planet: Planet
    val period: Double // time of single full rotation of satellite
    fun g() =
        4 * Math.PI.pow(2) * planet.radius.medium / ( planet.weight.normal * period.pow(2))
}

data class Planet (
    val radius: Radius,
    val weight: Weight
)

data class Radius (
    val big: Double,
    val medium: Double,
    val small: Double
)

data class Weight(
    val heavy: Double,
    val normal: Double,
    val light: Double
)