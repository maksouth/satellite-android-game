package name.mharbovskyi.satellitegame.presentation.play

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.media.Image
import android.util.AttributeSet
import android.view.View
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.ObjectState
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot

class PlaySurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private val planetPaint: Paint = Paint()
    private val planetPath: Path = Path()

    private val satellitePaint: Paint = Paint()
    private val satellitePath: Path = Path()

    private val targetPaint = Paint()
    private val targetPath = Path()

    init {
        planetPaint.isAntiAlias = true
        planetPaint.color = Color.BLUE
        planetPaint.style = Paint.Style.FILL
        planetPaint.strokeJoin = Paint.Join.MITER

        satellitePaint.isAntiAlias = true
        satellitePaint.color = Color.GREEN
        satellitePaint.style = Paint.Style.FILL
        satellitePaint.strokeJoin = Paint.Join.MITER

        targetPaint.isAntiAlias = true
        targetPaint.color = Color.RED
        targetPaint.style = Paint.Style.FILL
        targetPaint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(planetPath, planetPaint)
        canvas.drawPath(satellitePath, satellitePaint)
        canvas.drawPath(targetPath, targetPaint)
    }

    fun drawPlanet(planet: Planet) {
        planetPath.reset()

        planetPath.addCircle(
            planet.location.x.toFloat(),
            planet.location.y.toFloat(),
            planet.radius.toFloat(),
            Path.Direction.CW
        )

        invalidate()
    }

    fun drawPlanets(planets: List<Planet>) {
        planetPath.reset()

        planets.forEach{
            planetPath.addCircle(
                it.location.x.toFloat(),
                it.location.y.toFloat(),
                it.radius.toFloat(),
                Path.Direction.CW
            )
        }

        invalidate()
    }

    fun drawSatellite(satellite: Location) {
        satellitePath.reset()

        satellitePath.addCircle(
            satellite.x.toFloat(),
            satellite.y.toFloat(),
            20.toFloat(),
            Path.Direction.CW
        )

        invalidate()
    }

    fun drawTarget(target: TargetSpot) {
        redrawCircle(
            target.location.x,
            target.location.y,
            target.radius,
            targetPath
        )
    }

    private fun redrawCircle(x: Double, y: Double, radius: Double, path: Path) {
        path.reset()

        path.addCircle(
            x.toFloat(),
            y.toFloat(),
            radius.toFloat(),
            Path.Direction.CW
        )

        invalidate()
    }

}