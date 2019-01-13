package name.mharbovskyi.satellitegame.presentation.play.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot
import android.graphics.BitmapFactory



class PlaySurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private val planetDrawable: Drawable

    private val satellitePaint: Paint = Paint()
    private val satellitePath: Path = Path()

    private val targetPaint = Paint()
    private val targetPath = Path()

    private val trajectoryTail = Path()
    private val trajectoryPaint = Paint()

    private val trajectoryHint = Path()
    private val trajectoryHintPaint = Paint()

    private var listener: ((MotionEvent) -> Unit)? = null

    private var frameInvalidationThread: Thread? = null

    init {
        planetDrawable = context.resources.getDrawable(R.drawable.jupiter)

        satellitePaint.isAntiAlias = true
        satellitePaint.color = Color.GREEN
        satellitePaint.style = Paint.Style.FILL
        satellitePaint.strokeJoin = Paint.Join.MITER

        targetPaint.isAntiAlias = true
        targetPaint.color = Color.RED
        targetPaint.style = Paint.Style.FILL
        targetPaint.strokeJoin = Paint.Join.ROUND

        trajectoryPaint.color = Color.GRAY
        trajectoryPaint.style = Paint.Style.STROKE
        trajectoryPaint.strokeJoin = Paint.Join.MITER

        trajectoryHintPaint.color = Color.BLACK
        trajectoryHintPaint.style = Paint.Style.FILL
        trajectoryHintPaint.strokeJoin = Paint.Join.MITER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        canvas.drawPath(planetPath, planetPaint)
        planetDrawable.draw(canvas)
        canvas.drawPath(satellitePath, satellitePaint)
        canvas.drawPath(targetPath, targetPaint)
        canvas.drawPath(trajectoryTail, trajectoryPaint)
        canvas.drawPath(trajectoryHint, trajectoryHintPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        listener?.invoke(event)
        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    fun start() {
        frameInvalidationThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                Thread.sleep(1000/60)
                invalidate()
            }
        }
        frameInvalidationThread?.start()
    }

    fun stop() {
        frameInvalidationThread?.interrupt()
    }

    fun setTouchEventListener(listener: (MotionEvent) -> Unit) {
        this.listener = listener
    }

    fun clearTrajectoryTail() {
        trajectoryTail.reset()
    }

    fun drawPlanet(planet: Planet) {

        planetDrawable.setBounds(
            (planet.location.x - planet.radius).toInt(),
            (planet.location.y - planet.radius).toInt(),
            (planet.location.x + planet.radius).toInt(),
            (planet.location.y + planet.radius).toInt()
        )
    }

    fun drawSatellite(satellite: Location) {
        satellitePath.reset()

        trajectoryTail.addCircle(
            satellite.x.toFloat(),
            satellite.y.toFloat(),
            5f,
            Path.Direction.CW
        )

        satellitePath.addCircle(
            satellite.x.toFloat(),
            satellite.y.toFloat(),
            20.toFloat(),
            Path.Direction.CW
        )
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
    }

    fun drawTrajectoryHint(trajectory: List<Location>) {
        trajectoryHint.reset()

        trajectory.forEach {
            trajectoryHint.addCircle(
                it.x.toFloat(),
                it.y.toFloat(),
                8f,
                Path.Direction.CW)
        }
    }

    fun clearTrajectoryHint() {
        trajectoryHint.reset()
    }

}