package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.Location

interface CoordinateTransformer {
    fun transformLocation(location: Location): Location
    fun transformValue(value: Double): Double
}

class ScreenCoordinateTransformer(
    private val scale: Double,
    private val width: Int,
    private val height: Int
): CoordinateTransformer {

    override fun transformLocation(location: Location): Location {
        val scaleX = scaled(location.x, scale)
        val scaleY = scaled(location.y, scale)

        val movedX = scaleX + width/2
        val movedY = scaleY + height/2

        return Location(movedX, movedY)
    }

    override fun transformValue(value: Double): Double =
            scaled(value, scale)

}

internal fun scaled(value: Double, scale: Double) =
        value / scale
