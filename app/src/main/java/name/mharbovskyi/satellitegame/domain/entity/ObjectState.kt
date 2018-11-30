package name.mharbovskyi.satellitegame.domain.entity

import kotlin.math.pow
import kotlin.math.sqrt

open class ObjectState(
    val speed: Speed,
    val acceleration: Acceleration,
    val location: Location
) {
    override fun toString(): String =
        "ObjectState(speed=$speed, acceleration=$acceleration, location=$location)"
}

fun ObjectState.copy(
    speed: Speed = this.speed,
    acceleration: Acceleration = this.acceleration,
    location: Location = this.location
) = ObjectState(speed, acceleration, location)

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

fun Location.distanceTo(other: Location) =
    sqrt( (x - other.x).pow(2) + (y - other.y).pow(2) )