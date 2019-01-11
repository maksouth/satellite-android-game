package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot
import name.mharbovskyi.satellitegame.domain.entity.copy

interface CoordinateTransformer {
    fun transformPlanet(planet: Planet): Planet
    fun transformLocation(location: Location): Location
    fun transformTarget(target: TargetSpot): TargetSpot
}

class ScreenCoordinateTransformer(
    private val scale: Double,
    private val width: Int,
    private val height: Int
): CoordinateTransformer {

    override fun transformPlanet(planet: Planet): Planet =
        planet.copy(
            radius = scaled(planet.radius, scale = scale),
            location = transformLocation(planet.location)
        )

    override fun transformLocation(location: Location): Location {
        val scaleX = scaled(location.x, scale)
        val scaleY = scaled(location.y, scale)

        val movedX = scaleX + width/2
        val movedY = scaleY + height/2

        return Location(movedX, movedY)
    }

    override fun transformTarget(target: TargetSpot): TargetSpot =
            target.copy(
                radius = scaled(target.radius, scale = scale),
                location = transformLocation(target.location)
            )
}

internal fun scaled(value: Double, scale: Double) =
        value / scale
