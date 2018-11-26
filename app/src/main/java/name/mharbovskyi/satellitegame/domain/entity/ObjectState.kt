package name.mharbovskyi.satellitegame.domain.entity

open class ObjectState(
    val speed: Speed,
    val acceleration: Acceleration,
    val location: Location
) {
    override fun toString(): String =
        "ObjectState(speed=$speed, acceleration=$acceleration, location=$location)"
}

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