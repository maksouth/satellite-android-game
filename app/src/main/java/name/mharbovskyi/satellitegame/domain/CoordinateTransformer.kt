package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.Location

fun coordinateTransformer(width: Int, height: Int): (Location) -> Location {

    val scale = height.toDouble() / (12 * 100_000)

    return { location ->

        val scaleX = scaled(location.x, scale)
        val scaleY = scaled(location.y, scale)

        val movedX = scaleX + width/2
        val movedY = scaleY + height/2

        Location(movedX, movedY)
    }
}

internal fun scaled(value: Double, scale: Double) =
        value * scale / ( 12 * 100000 )
