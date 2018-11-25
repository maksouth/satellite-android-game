package name.mharbovskyi.satellitegame.physics.entity

open class ObjectState(
    val speed: Speed,
    val acceleration: Acceleration,
    val location: Location
)

interface Projection {
    val x: Double
    val y: Double
}

data class Speed(
    override val x: Double,
    override val y: Double
): Projection

data class Acceleration(
    override val x: Double,
    override val y: Double
): Projection

data class Location(
    override val x: Double,
    override val y: Double
): Projection