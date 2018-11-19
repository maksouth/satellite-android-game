package name.mharbovskyi.satellitegame.physics.entity

open class ObjectState(
    val speed: Speed,
    val acceleration: Acceleration,
    val location: Location
)

sealed class Projection(
    val x: Double,
    val y: Double
)

class Speed(x: Double, y: Double): Projection(x, y)
class Acceleration(x: Double, y: Double): Projection(x, y)
class Location(x: Double, y: Double): Projection(x, y)