package name.mharbovskyi.satellitegame.physics.entity

class Planet(
    val name: String,
    val mass: Double,
    val radius: Double,
    speed: Speed,
    acceleration: Acceleration,
    location: Location
): ObjectState(speed, acceleration, location)