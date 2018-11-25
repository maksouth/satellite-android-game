package name.mharbovskyi.satellitegame.physics

import org.junit.Assert.assertEquals
import name.mharbovskyi.satellitegame.physics.entity.Acceleration
import name.mharbovskyi.satellitegame.physics.entity.Location
import name.mharbovskyi.satellitegame.physics.entity.ObjectState
import name.mharbovskyi.satellitegame.physics.entity.Speed
import org.junit.Test
import kotlin.math.sqrt

class CalculationsKtTest {

    object Earth {
        val mass = 5.97e24
        val radius = 6371000
    }

    val `primary orbit speed` =
        sqrt(G * Earth.mass / Earth.radius)

    @Test fun `test primary orbit speed`() =
        println("$`primary orbit speed` ${Earth.mass}")


    @Test fun `test distance between objects`() {
        val first = Location(3.0, 4.0)
        val second = Location(0.0, 0.0)

        assertEquals(5.0, first.distanceTo(second), 0.0)
    }

    @Test fun `test location projection change`() {
        val newCoordinate = nextLocationProjection(0.0, 2.0, 5.0)

        assertEquals(10.0, newCoordinate, 0.0)
    }

    @Test fun `test location projection change negative speed`() {
        val newCoordinate = nextLocationProjection(0.0, -2.0, 5.0)

        assertEquals(-10.0, newCoordinate, 0.0)
    }

    @Test fun `test location change`() {
        val satellite = ObjectState(
            Speed(3.0, -4.0),
            Acceleration(0.0, 0.0),
            Location(0.0, 0.0)
        )

        val newLocation = nextLocation(satellite, 1.0)

        assertEquals(Location(3.0, -4.0), newLocation)
    }
}