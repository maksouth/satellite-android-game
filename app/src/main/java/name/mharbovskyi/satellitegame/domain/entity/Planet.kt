package name.mharbovskyi.satellitegame.domain.entity

class Planet(
    val name: String,
    val mass: Double,
    val radius: Double,
    location: Location,
    speed: Speed = Speed(0.0, 0.0),
    acceleration: Acceleration = Acceleration(0.0, 0.0)
): ObjectState(speed, acceleration, location)