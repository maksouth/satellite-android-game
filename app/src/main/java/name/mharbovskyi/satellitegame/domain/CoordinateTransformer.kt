package name.mharbovskyi.satellitegame.domain

import android.util.Log
import name.mharbovskyi.satellitegame.domain.entity.Location

fun coordinateTransformer(scale: Double, width: Int, height: Int): (Location) -> Location {

    return { location ->

        val scaleX = scaled(location.x, scale)
        val scaleY = scaled(location.y, scale)

        val movedX = scaleX + width/2
        val movedY = scaleY + height/2

        val to = Location(movedX, movedY)
        Log.d("CoordinateTransformer", "From: $location to $to")

        to
    }
}

internal fun scaled(value: Double, scale: Double) =
        value / scale
