package name.mharbovskyi.satellitegame.presentation.play

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.View

class PlaySurfaceView(context: Context): View(context) {

    private val circlePaint: Paint = Paint()
    private val circlePath: Path = Path()

    init {
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.BLUE
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeJoin = Paint.Join.MITER
        circlePaint.strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        circlePath.addCircle(width / 2f, height / 2f, width / 6f, Path.Direction.CW)
        canvas.drawPath(circlePath, circlePaint)
    }

}