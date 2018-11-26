package name.mharbovskyi.satellitegame.domain

import name.mharbovskyi.satellitegame.domain.entity.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class CoordinateTransformerKtTest {
    @Test fun `test coordinate begin is moved`() {
        val transformer = coordinateTransformer(100, 200)
        assertEquals(Location(50.0, 100.0), transformer(Location(0.0, 0.0)))
    }
}