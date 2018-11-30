package name.mharbovskyi.satellitegame.domain.entity

data class PlanetSystem(
    val planets: List<Planet>
)

class Planet(
    val name: String,
    val mass: Double,
    val radius: Double,
    location: Location,
    speed: Speed = Speed(0.0, 0.0),
    acceleration: Acceleration = Acceleration(0.0, 0.0)
): ObjectState(speed, acceleration, location)


fun Planet.copy(
    name: String = this.name,
    mass: Double = this.mass,
    radius: Double = this.radius,
    location: Location = this.location,
    speed: Speed = this.speed,
    acceleration: Acceleration = this.acceleration
): Planet =
    Planet(name, mass, radius, location, speed, acceleration)