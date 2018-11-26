package name.mharbovskyi.satellitegame.domain.physics

import kotlinx.coroutines.channels.take
import kotlinx.coroutines.channels.withIndex
import kotlinx.coroutines.runBlocking
import name.mharbovskyi.satellitegame.domain.entity.*
import org.junit.Assert.assertEquals
import name.mharbovskyi.satellitegame.domain.intervalTimer
import name.mharbovskyi.satellitegame.domain.scaledTimer
import org.junit.Test
import kotlin.math.pow
import kotlin.math.sqrt

class PhysicsCalculationKtTest {

    object Earth {
        val mass = 5.97e24
        val radius = 6371000
    }

    val `primary orbit speed` =
        sqrt(G * Earth.mass / Earth.radius)

    @Test fun `test primary orbit speed`() =
        println("$`primary orbit speed` ${Earth.mass* G}")


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

    @Test fun `fake G`() = runBlocking {
        val g = 2 * Math.PI.pow(2) / 5
        val timer = scaledTimer(1e-3, intervalTimer(1))

        val planet = Planet(
            "Ventura",
            100.0,
            10.0,
            Location(0.0, 0.0)
        )

        val initialSpeed = sqrt( g * planet.mass / planet.radius )
        var satellite = ObjectState(
            Speed(0.8 * initialSpeed, 0.0),
            Acceleration(0.0, 0.0),
            Location(0.0, planet.radius)
        )

        for ((index, time) in timer.take(30000).withIndex()) {
            println("Index $index $time")
            println(satellite)
            println("Distance to center ${satellite.location.distanceTo(planet.location)}")
            satellite = nextObjectState(satellite, planet, time, gravitationalConstant = g)
        }
    }
}