package name.mharbovskyi.satellitegame.domain.physics.measurement

/**
 * system in which satellite near the ground
 * has a period of rotation 10 s
 * around planet with radius 10 m and weight 100 kg
 */
object FirstMeasurementSystem: MeasurementSystem {
    override val planet = Planet(
        Radius(big = 20.0, medium = 10.0, small = 5.0),
        Weight(heavy = 200.0, normal = 100.0, light = 50.0)
    )

    override val period = 10.0
}